/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bo.impl;

import com.lapool.bo.PoolBO;
import com.lapool.bo.PoolExtBO;
import com.lapool.bo.PushBO;
import com.lapool.client.OneSignalRESTClient;
import com.lapool.client.UserRestClient;
import com.lapool.dao.NetworkDAO;
import com.lapool.dao.PoolDAO;
import com.lapool.dao.UserDAO;
import com.lapool.data.AlertDataBean;
import com.lapool.data.ChangeStatusBean;
import com.lapool.data.HopResult;
import com.lapool.data.fb.FBData;
import com.lapool.data.fb.FBPaging;
import com.lapool.data.fb.FacebookJSONResult;
import com.lapool.data.LocationDetails;
import com.lapool.data.LocationPoint;
import com.lapool.data.PickUpRequestBean;
import com.lapool.data.PushNotification;
import com.lapool.data.TripBean;
import com.lapool.data.TripDetails;
import com.lapool.exception.BaseException;
import com.lapool.model.Alert;
import com.lapool.model.AlertLocation;
import com.lapool.model.AuthProvider;
import com.lapool.model.Channel;
import com.lapool.model.PickUpRequest;
import com.lapool.model.Trip;
import com.lapool.model.TripLocation;
import com.lapool.util.ApplicationConstants;
import com.lapool.util.SystemUtils;
import com.owlike.genson.Genson;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author akshat
 */
public class PoolBOimpl implements PoolBO {
    
    final static Logger log = LoggerFactory.getLogger(PoolBOimpl.class);
    
    private String baseURL;
    private String oneSignalAppID;
    private PoolExtBO poolExtBO;
    private PushBO pushBO;
    private NetworkDAO networkDAO;
    private PoolDAO poolDAO;
    private UserDAO userDAO;
    private UserRestClient userRestClient;
    private OneSignalRESTClient oneSignalRESTClient;

    public void setPushBO(PushBO pushBO) {
        this.pushBO = pushBO;
    }

    public void setPoolExtBO(PoolExtBO poolExtBO) {
        this.poolExtBO = poolExtBO;
    }
    
    public void setOneSignalAppID(String oneSignalAppID) {
        this.oneSignalAppID = oneSignalAppID;
    }
    
    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }
    
    public void setPoolDAO(PoolDAO poolDAO) {
        this.poolDAO = poolDAO;
    }
    
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    
    public void setNetworkDAO(NetworkDAO networkDAO) {
        this.networkDAO = networkDAO;
    }
    
    public void setUserRestClient(UserRestClient userRestClient) {
        this.userRestClient = userRestClient;
    }
        
    public void setOneSignalRESTClient(OneSignalRESTClient oneSignalRESTClient) {
        this.oneSignalRESTClient = oneSignalRESTClient;
    }


    @Override
    @Transactional
    public HopResult registerPickUpRequest(PickUpRequestBean pickUpRequestBean) throws BaseException {
        final String methodName = "registerPickUpRequest()";
        log.info("Entering :" + methodName);
        
        long pickReqID;
        AuthProvider authProvider = null;

        authProvider = userDAO.fetchAuthProvider(pickUpRequestBean.getAuthID());
        
        //Check if the user is active before proceeding
        if(!SystemUtils.isAuthProviderActive(authProvider.getStatus())){
            throw new BaseException("User is inactive");
        }
        authProvider.setId(pickUpRequestBean.getAuthID());

        PickUpRequest pickReq = new PickUpRequest();
        pickReq.setAuthProvider(authProvider);
        pickReq.setStatus(ApplicationConstants.STATUS_PICKREQ_NEW);
        pickReq.setCreated(new Timestamp(System.currentTimeMillis()));
        pickReq.setUpdated(new Timestamp(System.currentTimeMillis()));
        pickReq.setPickUpLat(pickUpRequestBean.getPickUpLat());
        pickReq.setPickUpLng(pickUpRequestBean.getPickUpLng());
        pickReq.setPickAddress(pickUpRequestBean.getPickAddress());
        pickReq.setDropLat(pickUpRequestBean.getDropLat());
        pickReq.setDropLng(pickUpRequestBean.getDropLng());
        pickReq.setDropLng(pickUpRequestBean.getDropLng());
        pickReq.setDropAddress(pickUpRequestBean.getDropAddress());
        pickReq.setGirlsOnly(pickUpRequestBean.isGirlsOnly() ? 1 : 0);
        pickReq.setMusic(pickUpRequestBean.isMusic() ? 1 : 0);
        pickReq.setSmoking(pickUpRequestBean.isSmoking() ? 1 : 0);
        pickReq.setOpenToAll(pickUpRequestBean.isOpenToAll()? 1 : 0);
        pickReq.setSeats(pickUpRequestBean.getSeats());
        
        pickReqID = poolDAO.registerPickupRequest(pickReq);
        if (pickReqID <= 0) {
            log.error("Error creating Pick Request");
            throw new BaseException("Error creating request");
        }
        
        authProvider = userDAO.fetchAuthProvider(pickUpRequestBean.getAuthID());
        pickUpRequestBean.setPickID(pickReqID);
        
        List userIDs = notifyChannels(authProvider, pickUpRequestBean);
        
        //Notify PATH users who are in the similar route and time
        if (pickUpRequestBean.isOpenToAll()) {
            try {
                poolExtBO.notifyMatchedPathUsers(pickUpRequestBean, userIDs, authProvider, pickReqID);
            } catch (BaseException be) {
                log.error("Error notifying Path Users" + be.getMessage());
            }

        }
        HopResult hopResult = new HopResult();
        hopResult.setPickID(pickReqID);
        if(userIDs != null)
        {
            hopResult.setNotifiedUsers(userIDs.size());
        }
        return hopResult;
    }
    
    @Override
    @Transactional
    public List getPickRequestsForAuthID(long authID, double lat, double lng, int pageNumber, int pageSize) throws BaseException {

        // This code is specific to FB logins but can be altered to include other social media logins
        final String methodName = "getPickRequestsForAuthID()";
        log.info("Entering :" + methodName);

        //HashSet<Long> idSet = new HashSet<Long>();
        HashMap<Long,ArrayList<Integer>> usersMap = new HashMap<Long, ArrayList<Integer>>();

        AuthProvider authProvider = userDAO.fetchAuthProvider(authID);

        //Find FB friends with app installed
        // If user has logged in using FB then his userID will be in Auth table itself
        if (authProvider.getProviderType() == ApplicationConstants.PROVIDER_TYPE_FACEBOOK) {
            String jsonFriendList = this.userRestClient.getFBFriendListWithInstalledApp(authProvider.getProviderId(), authProvider.getFbUserLogin().getToken());

            Genson gen = new Genson();
            FacebookJSONResult fbDataList = gen.deserializeInto(jsonFriendList, new FacebookJSONResult());

            if (null != jsonFriendList || fbDataList.getData().length > 0) {
                FBData[] users = fbDataList.getData();
                List<FBData> userList = new ArrayList<FBData>();
                userList.addAll(Arrays.asList(users));
                FBPaging paging = fbDataList.getPaging();

                //Check if more FB paging data needs to be fetched via paging
                if (paging != null) {
                    while (paging.getNext() != null) {
                        String nextSet = this.userRestClient.getNextFBDataSet(paging.getNext());

                        fbDataList = gen.deserializeInto(nextSet, new FacebookJSONResult());
                        users = fbDataList.getData();
                        userList.addAll(Arrays.asList(users));

                        paging = fbDataList.getPaging();
                        if (paging == null) {
                            break;
                        }
                    }
                }

                //Find each FB user's authID
                List<String> fbUserIds = new ArrayList<String>();
                for (FBData user : userList) {
                    fbUserIds.add(user.getId());
                }
                
                List<Long> authIds = null;
                if(!fbUserIds.isEmpty())
                {
                    authIds = userDAO.getListOfActiveAuthIds(fbUserIds, ApplicationConstants.PROVIDER_TYPE_FACEBOOK);
                }
                if (null != authIds && !authIds.isEmpty()) {
                    //idSet.addAll(authIds);
                    //HashMap code
                    for(Long id : authIds){
                        SystemUtils.addToList(id, ApplicationConstants.NETWORK_FACEBOOK, usersMap);
                    }
                }
                
            }
        }

        //2. Find authIDs who have subscribed to same channel
        //Right now it goes up towards the parent and fetches their channelIDs
        //TODO : This has to be improved for performance
        List<Channel> channels = networkDAO.getUserChannelsFromAuthID(authProvider.getId());
        Set<Long> parentChannelIDs = new HashSet<Long>();
        List<Long> usersSubcribed = null;
        if (channels != null && !channels.isEmpty()) {
            for (Channel chan : channels) {
                parentChannelIDs.add(chan.getId());
                while (chan.getParent() != null) {
                    chan = chan.getParent();
                    parentChannelIDs.add(chan.getId());
                }
            }
            //For each channel ID found find subscribed authIDs
            usersSubcribed = networkDAO.getActiveUsersSubscribed(new ArrayList(parentChannelIDs));
            usersSubcribed.remove(authProvider.getId());
            //idSet.addAll(usersSubcribed);
            //HashMap
            for (Long id : usersSubcribed) {
                SystemUtils.addToList(id, ApplicationConstants.NETWORK_WORK, usersMap);
            }
            
        }
        
//        if (idSet.size() <= 0) {
//            log.info(methodName + ": No matching AuthIDs found for the request");
//            return null;
//        }
        
        if (usersMap.isEmpty()) {
            log.info(methodName + ": No matching AuthIDs found for the request");
            return null;
        }

        // search for these authIDs in pickRequests table but make sure :-
        // 1. The pickRequests are 30min old
        // 2. The pickRequests are in status STATUS_PICK_REQUESTED
        // Kilometer range is set to 5km here
        Timestamp minusTime = new Timestamp(System.currentTimeMillis() - ApplicationConstants.PICKUPREQ_EXPIRE_TIME_IN_SECS);
        
        LocationPoint p1 = SystemUtils.calculateDerivedPosition(lat, lng, 5000, 0);
        LocationPoint p2 = SystemUtils.calculateDerivedPosition(lat, lng, 5000, 90);
        LocationPoint p3 = SystemUtils.calculateDerivedPosition(lat, lng, 5000, 180);
        LocationPoint p4 = SystemUtils.calculateDerivedPosition(lat, lng, 5000, 270);
        
        boolean listFemalePickReqs = true;
        if (authProvider.getGender() == 'm') {
            listFemalePickReqs = false;
        }

        List<PickUpRequest> pickReqs = poolDAO.getPickupRequests(usersMap.keySet(), minusTime, ApplicationConstants.STATUS_PICKREQ_NEW, p1, p2, p3, p4, pageNumber, pageSize, listFemalePickReqs);

        List<PickUpRequestBean> pickupReqsBeans = new ArrayList<PickUpRequestBean>();
        if (null != pickReqs) {
            for (PickUpRequest pr : pickReqs) {
                PickUpRequestBean pick = new PickUpRequestBean();
                pick.setAge(SystemUtils.calculateAge(pr.getAuthProvider().getBirthday()));
                pick.setAuthID(pr.getAuthProvider().getId());
                pick.setUserID(pr.getAuthProvider().getProviderId());
                pick.setDropAddress(pr.getDropAddress());
                pick.setDropLat(pr.getDropLat());
                pick.setDropLng(pr.getDropLng());
                pick.setGender(pr.getAuthProvider().getGender());
                pick.setGirlsOnly(pr.getGirlsOnly() == 1);
                pick.setMusic(pr.getMusic() == 1);
                pick.setName(pr.getAuthProvider().getName());
                pick.setPickAddress(pr.getPickAddress());
                pick.setPickUpLat(pr.getPickUpLat());
                pick.setPickUpLng(pr.getPickUpLng());
                pick.setSmoking(pr.getSmoking() == 1);
                pick.setCreated(pr.getCreated());
                pick.setPickID(pr.getPickID());
                pick.setDistanceFromUser(SystemUtils.distFrom(lat, lng, pr.getPickUpLat(), pr.getPickUpLng()));
                pick.setNetworkLinked(usersMap.get(pr.getAuthProvider().getId()));
                pick.setSeats(pr.getSeats());
                pickupReqsBeans.add(pick);
            }
        }
        
        log.info("Exiting :" + methodName);
        return pickupReqsBeans;
    }

    /**
     *
     * @param authID - AuthID of the user who confirmed to pick the other user
     * @param pickID - Unique ID of the pick request
     * @param dropperLat
     * @param dropperLng
     * @throws BaseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized long confirmPickupRequest(long authID, long pickID, double dropperLat, double dropperLng) throws BaseException {
        final String methodName = "confirmPickupRequest()";
        log.info("Entering :" + methodName);

        //TODO:
        // This has to be thread safe - synchronized method check performance
        //Check if pickup request is cancelled before updating
        //Change status of pickUpRequest
        PickUpRequest pick = poolDAO.getPickUpRequest(pickID);

        //If pick Req is cancelled or expired
        if (pick.getStatus() == ApplicationConstants.STATUS_PICKREQ_CANCELLED
                || ((System.currentTimeMillis() - pick.getCreated().getTime()) > ApplicationConstants.PICKUPREQ_EXPIRE_TIME_INTERVAL)) {
            log.info("Pickup request already cancelled :" + methodName);
            throw new BaseException("Request cancelled");
        }

        //If pickupRequest is already accepted by another user or state has changed
        if (pick.getStatus() == ApplicationConstants.STATUS_PICKREQ_ACCEPTED) {
            throw new BaseException(ApplicationConstants.GENERIC_SERVER_ERROR, ApplicationConstants.ERROR_CODE_TRIP_ALREADY_CONFIRMED,
                    ApplicationConstants.MSG_TRIP_ALREADY_ACCEPTED, ApplicationConstants.MSG_TRIP_ALREADY_ACCEPTED, this.baseURL);
        }
        pick.setStatus(ApplicationConstants.STATUS_PICKREQ_ACCEPTED);
        pick.setUpdated(new Timestamp(System.currentTimeMillis()));
        


        //Create a trip record
        Trip trip = new Trip();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        AuthProvider authProvider = userDAO.fetchAuthProvider(authID);
        authProvider.setId(authID);
        trip.setHostStartLat(dropperLat);
        trip.setHostStartLng(dropperLng);
        trip.setAuthProvider(authProvider);
        trip.setPickUpRequest(pick);
        trip.setStartTime(now);
        trip.setCreated(now);
        trip.setUpdated(now);
        trip.setStatus(ApplicationConstants.STATUS_TRIP_CREATED);
        trip.setTripKey(randomTripKey());
        pick.setTrip(trip);

        //Save the trip details
        long triID = poolDAO.saveTrip(trip);

        // Send notification to the user to be picked 
        // This is specific to android devices
        //--------- start ------------
        PushNotification pushNotification = new PushNotification();
        pushNotification.setApp_id(this.oneSignalAppID);

        HashMap<String, Object> data = new HashMap<String, Object>();
        Genson gen = new Genson();

        data.put("data_type", ApplicationConstants.PUSH_TYPE_PICKREQ_ACCEPTED);
        data.put("tripID", trip.getTripID());

        pushNotification.setData(data);
//        HashMap<String, String> tags = new HashMap<String, String>();
//        tags.put("key", "authID");
//        tags.put("relation", "=");
//        tags.put("value", Long.toString(pick.getAuthProvider().getId()));
//        ArrayList tagList = new ArrayList();
//        tagList.add(tags);
        HashMap<String, String> filters = new HashMap<String, String>();
        ArrayList filterList = new ArrayList();
        filters.put("field", "tag");
        filters.put("key", "authID");
        filters.put("relation", "=");
        filters.put("value", Long.toString(pick.getAuthProvider().getId()));
        filterList.add(filters);

        HashMap<String, String> headings = new HashMap<String, String>();
        headings.put("en", "Your HOP is confirmed");

        HashMap<String, String> contents = new HashMap<String, String>();
        contents.put("en", authProvider.getName() + " is picking you up.");
        
        //pushNotification.setTags(tagList);
        pushNotification.setFilters(filterList);
        pushNotification.setHeadings(headings);
        pushNotification.setContents(contents);
        pushNotification.setAndroid_background_data(true);
        oneSignalRESTClient.pushNotification(gen.serialize(pushNotification));
        // ---------- stop ------------

        log.info("Exiting :" + methodName);
        
        return triID;
    }
    
    /**
     * Generates a random trip key and checks for duplicates in DB
     * @return
     * @throws BaseException 
     */
    private String randomTripKey() throws BaseException {
        String key = SystemUtils.generateUniqueID();
        if (poolDAO.isTripKeyExist(key)) {
            key = randomTripKey();
        }
        return key;
    }
    
    @Override
    @Transactional
    public TripBean getTripDetails(long tripID) throws BaseException {
        final String methodName = "getTripDetails()";
        log.info("Entering :" + methodName);
        
        Trip trip = poolDAO.getTripDetails(tripID);
        TripBean tripBean = new TripBean();

        //PickupRequest lat and lng
        tripBean.setPickupLat(trip.getPickUpRequest().getPickUpLat());
        tripBean.setPickupLng(trip.getPickUpRequest().getPickUpLng());
        
        tripBean.setDropLat(trip.getPickUpRequest().getDropLat());
        tripBean.setDropLng(trip.getPickUpRequest().getDropLng());
        
        tripBean.setHopperID(trip.getPickUpRequest().getAuthProvider().getId());
        tripBean.setDropperID(trip.getAuthProvider().getId());
        
        tripBean.setHopperProviderID(trip.getPickUpRequest().getAuthProvider().getProviderId());
        tripBean.setDropperProviderID(trip.getAuthProvider().getProviderId());
        
        tripBean.setHopperName(trip.getPickUpRequest().getAuthProvider().getName());
        tripBean.setDropperName(trip.getAuthProvider().getName());
        
        tripBean.setHopperSex(trip.getPickUpRequest().getAuthProvider().getGender());
        tripBean.setDropperSex(trip.getAuthProvider().getGender());
        
        tripBean.setHopperAge(SystemUtils.calculateAge(trip.getPickUpRequest().getAuthProvider().getBirthday()));
        tripBean.setDropperAge(SystemUtils.calculateAge(trip.getAuthProvider().getBirthday()));
        
        tripBean.setIsGirlsOnly(trip.getPickUpRequest().getGirlsOnly() == 1);
        tripBean.setIsNoSmoking(trip.getPickUpRequest().getSmoking() == 1);
        tripBean.setMusic(trip.getPickUpRequest().getMusic() == 1);
        
        tripBean.setPickAddress(trip.getPickUpRequest().getPickAddress());
        tripBean.setDropAddress(trip.getPickUpRequest().getDropAddress());
        
        tripBean.setDropperStartLat(trip.getHostStartLat());
        tripBean.setDropperStartLng(trip.getHostStartLng());
        
        tripBean.setStatus(trip.getStatus());
        tripBean.setTripKey(trip.getTripKey());
        
        
        if (trip.getStartTime() != null) {
            tripBean.setTripStartTime(trip.getStartTime().getTime());
        }
        if (trip.getStopTime() != null) {
            tripBean.setTripEndTime(trip.getStopTime().getTime());
        }
        
        return tripBean;
        
    }
    
    @Override
    @Transactional
    public long changePickupRequestStatus(long pickupID, int cancalType) throws BaseException {
        final String methodName = "changePickupRequestStatus()";
        log.info("Entering :" + methodName + " Status:"+cancalType);
        
        PickUpRequest pickReq = poolDAO.getPickUpRequest(pickupID);
        pickReq.setStatus(cancalType);
        pickReq.setUpdated(new Timestamp(System.currentTimeMillis()));
        long pickID = poolDAO.saveUpdatePickupRequest(pickReq);
        
        log.info("Exiting :" + methodName);
        return pickID;
    }

    @Override
    @Transactional
    public long endTrip(ChangeStatusBean tripCancelBean) throws BaseException {
        final String methodName = "endTrip() by "+tripCancelBean.getAuthID();
        log.info("Entering :" + methodName);
        
        Trip trip = poolDAO.getTripDetails(tripCancelBean.getId());
        if (trip.getStatus() == ApplicationConstants.STATUS_TRIP_ENDED_BY_DROPPER
                || trip.getStatus() == ApplicationConstants.STATUS_TRIP_ENDED_BY_HOPPER) {
            log.error("Trip alreday ended: " + methodName);
            return tripCancelBean.getId();
        }
        trip.setStatus(tripCancelBean.getStatus());
        trip.setUpdated(new Timestamp(System.currentTimeMillis()));
        trip.setStopTime(new Timestamp(System.currentTimeMillis()));
        long tripID = poolDAO.saveTrip(trip);

        // Send notification to the user to be picked 
        // This is specific to android devices
        //--------- start ------------
        PushNotification pushNotification = new PushNotification();
        pushNotification.setApp_id(this.oneSignalAppID);
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        Genson gen = new Genson();
        
        data.put("data_type", ApplicationConstants.PUSH_TYPE_TRIP_ENDED);
        data.put("tripID", trip.getTripID());
        
        pushNotification.setData(data);
//        HashMap<String, String> tags = new HashMap<String, String>();
//        tags.put("key", "authID");
//        tags.put("relation", "=");

        HashMap<String, String> filters = new HashMap<String, String>();
        
        filters.put("field", "tag");
        filters.put("key", "authID");
        filters.put("relation", "=");
        

        //If Dropper ended the trip
        if (trip.getAuthProvider().getId() == tripCancelBean.getAuthID()) {
            //Send notification to hopper
            //         tags.put("value", Long.toString(trip.getPickUpRequest().getAuthProvider().getId()));
            filters.put("value", Long.toString(trip.getPickUpRequest().getAuthProvider().getId()));
        } else {
            //send notification to dropper
            //tags.put("value", Long.toString(trip.getAuthProvider().getId()));
            filters.put("value", Long.toString(trip.getAuthProvider().getId()));
        }
        
        ArrayList filterList = new ArrayList();
        filterList.add(filters);
//        ArrayList tagList = new ArrayList();
//        tagList.add(tags);
        
        HashMap<String, String> contents = new HashMap<String, String>();
        contents.put("en", "Your trip has ended!");
        
        HashMap<String, String> headings = new HashMap<String, String>();
        headings.put("en", "Trip ended");
        
        pushNotification.setHeadings(headings);
        //pushNotification.setTags(tagList);
        pushNotification.setFilters(filterList);
        pushNotification.setContents(contents);
        pushNotification.setAndroid_background_data(true);
        oneSignalRESTClient.pushNotification(gen.serialize(pushNotification));
        // ---------- stop ------------

        log.info("Exiting :" + methodName);
        return tripID;
    }
    
    private List notifyChannels(AuthProvider authProvider, PickUpRequestBean pickUpRequestBean) throws BaseException {
        
        HashSet<Long> idSet = new HashSet<Long>();
        
        try {

            // 1. Find user's FB friends who have installed the app
            if (authProvider.getProviderType() == ApplicationConstants.PROVIDER_TYPE_FACEBOOK) {
                String jsonFriendList = this.userRestClient.getFBFriendListWithInstalledApp(authProvider.getProviderId(), authProvider.getFbUserLogin().getToken());
                
                Genson gen = new Genson();
                FacebookJSONResult fbDataList = gen.deserializeInto(jsonFriendList, new FacebookJSONResult());
                
                if (null != jsonFriendList || fbDataList.getData().length > 0) {
                    FBData[] users = fbDataList.getData();
                    List<FBData> userList = new ArrayList<FBData>();
                    userList.addAll(Arrays.asList(users));
                    FBPaging paging = fbDataList.getPaging();

                    //Check if more FB paging data needs to be fetched via paging
                    if (paging != null) {
                        while (paging.getNext() != null) {
                            String nextSet = this.userRestClient.getNextFBDataSet(paging.getNext());
                            
                            fbDataList = gen.deserializeInto(nextSet, new FacebookJSONResult());
                            users = fbDataList.getData();
                            userList.addAll(Arrays.asList(users));
                            
                            paging = fbDataList.getPaging();
                            if (paging == null) {
                                break;
                            }
                        }
                    }

                    //Find each FB user's authID
                    List<String> fbUserIds = new ArrayList<String>();
                    for (FBData user : userList) {
                        fbUserIds.add(user.getId());
                    }
                    List<Long> authIds = null;
                    if(!fbUserIds.isEmpty())
                    {
                        authIds = userDAO.getListOfActiveAuthIds(fbUserIds, ApplicationConstants.PROVIDER_TYPE_FACEBOOK);
                    }
                    if (null != authIds) {
                        idSet.addAll(authIds);
                    }
                    
                }
            }
            
        } catch (Exception e) {
            log.error("Error in FB processing .. continuing ahead :"+e.getMessage());
        }

        //2. Find authIDs who have subscribed to same channel
        //Right now it goes up towards the parent and fetches their channelIDs
        //TODO : This has to be improved for performance
//        try{
//        List<Channel> channels = networkDAO.getUserChannelsFromAuthID(authProvider.getId());
//        Set<Long> parentChannelIDs = new HashSet<Long>();
//        List<Long> usersSubcribed = null;
//        if (channels != null && !channels.isEmpty()) {
//            for (Channel chan : channels) {
//                if (chan.getStatus() == ApplicationConstants.STATUS_CHANNEL_ACTIVE) {
//                    parentChannelIDs.add(chan.getId());
//                }
//                while (chan.getParent() != null) {
//                    chan = chan.getParent();
//                    if (chan.getStatus() == ApplicationConstants.STATUS_CHANNEL_ACTIVE) {
//                        parentChannelIDs.add(chan.getId());
//                    }
//                }
//            }
//            //For each channel ID found find subscribed authIDs
//            usersSubcribed = networkDAO.getActiveUsersSubscribed(new ArrayList(parentChannelIDs));
//            usersSubcribed.remove(authProvider.getId());
//            idSet.addAll(usersSubcribed);
//            
//        }
//        }catch(Exception e){
//            log.error("Exception in Channel/User processing .. continuing : "+e.getMessage());
//        }
        
        
        //If no network people found - return 
        if(idSet.size() <= 0){
            log.info("No network people found");
            return null;
        }
        
        //findout which all authIDs are within a range of 5km
        // Send notification to the user who are closeby 5km
        List finalIDs = userDAO.fetchUsersByDistance(pickUpRequestBean.getPickUpLat(), pickUpRequestBean.getPickUpLng(), idSet, 5);

        
        if (finalIDs == null || finalIDs.isEmpty()) {
            return null;
        }

        pushBO.hopNotificationToListOfAuthIDs(finalIDs, pickUpRequestBean.getPickID(), authProvider.getName(), pickUpRequestBean.getDropAddress());

        // ---------- stop ------------
        return finalIDs;
        
    }

    @Override
    @Transactional
    public void updateUserTripLocation(long tripID, long authID, LocationPoint locPt) throws BaseException {
        final String methodName = "updateUserTripLocation()";
        log.info("Entering :" + methodName);

        TripLocation location = new TripLocation();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        location.setCreated(now);
        location.setUpdated(now);
        
        location.setLat(locPt.getLat());
        location.setLng(locPt.getLng());

        poolDAO.updateUserTripLocation(location, authID, tripID);

        log.info("Exiting :"+methodName);
    }

    @Override
    @Transactional
    public TripDetails fetchTripDetailsFromKey(String key) throws BaseException {
        final String methodName = "fetchTripDetailsFromKey()";
        log.info("Entering :" + methodName+" - Key: "+key);

        Trip trip = poolDAO.fetchTripDetails(key);

        if(trip == null){
            log.info("Invalid trip key :"+methodName);
            throw new BaseException();
        }
        
        TripDetails tripDetails = SystemUtils.copyTripToTripDetails(trip);

        log.info("Exiting :" + methodName);
        return tripDetails;
    }

    @Override
    @Transactional
    public TripDetails fetchTripDetails(long tripID, long hopperID, long dropperID) throws BaseException {
                final String methodName = "fetchTripLocations()";
        log.info("Entering  :" + methodName+" - tripID :"+tripID);

        TripDetails tripDetails = new TripDetails();
        ArrayList<LocationDetails> hopperRoute = new ArrayList<LocationDetails>();
        ArrayList<LocationDetails> dropperRoute = new ArrayList<LocationDetails>();
        
        List<TripLocation> locations = poolDAO.fetchTripLocations(tripID);
        if(locations != null & !locations.isEmpty()){
            for(TripLocation loc: locations){
                if(loc.getAuthProvider().getId() == hopperID){
                    LocationDetails lp = new LocationDetails();
                    lp.setLat(loc.getLat());
                    lp.setLng(loc.getLng());
                    lp.setCreated(loc.getCreated());
                    hopperRoute.add(lp);
                }else{
                    LocationDetails lp = new LocationDetails();
                    lp.setLat(loc.getLat());
                    lp.setLng(loc.getLng());
                    lp.setCreated(loc.getCreated());
                    dropperRoute.add(lp);
                }
            }
        }
        tripDetails.setHopperRoute(hopperRoute);
        tripDetails.setDropperRoute(dropperRoute);
        
        log.info("Exiting  :" + methodName);
        return tripDetails;
    }

    @Override
    @Transactional
    public AlertDataBean saveAlertDetails(AlertDataBean alertDataBean) throws BaseException {
        final String methodName = "saveAlertDetails()";
        log.info("Entering  :" + methodName+" - authID :"+alertDataBean.getAuthID());
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        
        Alert alert = new Alert();
        alert.setCreated(now);
        alert.setUpdated(now);
        alert.setAlertKey(SystemUtils.generateUniqueID());
        alert.setLat(alertDataBean.getLat());
        alert.setLng(alertDataBean.getLng());
        
        poolDAO.saveAlertDetails(alert, alertDataBean.getTripID(), alertDataBean.getAuthID());
        
        AlertDataBean result = new  AlertDataBean();
        result.setKey(alert.getAlertKey());
        result.setAlertID(alert.getId());
        
        log.info("Exiting  :" + methodName);
        return result;
    }

    @Override
    @Transactional
    public void updateAlertLocation(long alertID, LocationPoint location) throws BaseException {
        final String methodName = "updateAlertLocation()";
        log.info("Entering  :" + methodName+ " - alertID: "+alertID);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        AlertLocation alertLocation = new AlertLocation();
        
        alertLocation.setCreated(now);
        alertLocation.setLat(location.getLat());
        alertLocation.setLng(location.getLng());
        
        
        poolDAO.saveAlertLocation(alertLocation, alertID);

        log.info("Exiting  :" + methodName);
    }

    @Override
    @Transactional
    public List fetchPublicPickRequestsForAuthID(long authID, double lat, double lng, int pageNumber, int pageSize) throws BaseException {
        final String methodName = "fetchPublicPickRequestsForAuthID()";
        log.info("Entering  :" + methodName+ " - authID: "+authID);    

        List<PickUpRequest> pickReqs = null;

        if(authID <= 0 || lat <= 0 || lng <= 0){
            throw new BaseException("Null values present");
        }
        
        AuthProvider authProvider = userDAO.fetchAuthProvider(authID);
        // search for these authIDs in pickRequests table but make sure :-
        // 1. The pickRequests are 30min old
        // 2. The pickRequests are in status STATUS_PICK_REQUESTED
        // Kilometer range is set to 5km here
        Timestamp minusTime = new Timestamp(System.currentTimeMillis() - ApplicationConstants.PICKUPREQ_EXPIRE_TIME_IN_SECS);

        LocationPoint p1 = SystemUtils.calculateDerivedPosition(lat, lng, 5000, 0);
        LocationPoint p2 = SystemUtils.calculateDerivedPosition(lat, lng, 5000, 90);
        LocationPoint p3 = SystemUtils.calculateDerivedPosition(lat, lng, 5000, 180);
        LocationPoint p4 = SystemUtils.calculateDerivedPosition(lat, lng, 5000, 270);

        boolean listFemalePickReqs = true;
        if (authProvider.getGender() == 'm') {
            listFemalePickReqs = false;
        }

        pickReqs = poolDAO.fetchPublicPickRequests(authID, minusTime, ApplicationConstants.STATUS_PICKREQ_NEW, p1, p2, p3, p4, pageNumber, pageSize, listFemalePickReqs);

        List<PickUpRequestBean> pickupReqsBeans = new ArrayList<PickUpRequestBean>();
        if (null != pickReqs) {
            for (PickUpRequest pr : pickReqs) {
                PickUpRequestBean pick = new PickUpRequestBean();
                pick.setAge(SystemUtils.calculateAge(pr.getAuthProvider().getBirthday()));
                pick.setAuthID(pr.getAuthProvider().getId());
                pick.setUserID(pr.getAuthProvider().getProviderId());
                pick.setDropAddress(pr.getDropAddress());
                pick.setDropLat(pr.getDropLat());
                pick.setDropLng(pr.getDropLng());
                pick.setGender(pr.getAuthProvider().getGender());
                pick.setGirlsOnly(pr.getGirlsOnly() == 1);
                pick.setMusic(pr.getMusic() == 1);
                pick.setName(pr.getAuthProvider().getName());
                pick.setPickAddress(pr.getPickAddress());
                pick.setPickUpLat(pr.getPickUpLat());
                pick.setPickUpLng(pr.getPickUpLng());
                pick.setSmoking(pr.getSmoking() == 1);
                pick.setCreated(pr.getCreated());
                pick.setPickID(pr.getPickID());
                pick.setDistanceFromUser(SystemUtils.distFrom(lat, lng, pr.getPickUpLat(), pr.getPickUpLng()));
                pick.setSeats(pr.getSeats());
                pickupReqsBeans.add(pick);
            }
        }

        log.info("Exiting  :" + methodName+" - Result size:"+pickupReqsBeans.size());
        return pickupReqsBeans;
    }
    
    // ------------------- NO MORE METHODS IN  THIS CLASS ----------------------------

}