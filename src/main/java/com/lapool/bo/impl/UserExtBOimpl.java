package com.lapool.bo.impl;

import com.lapool.bo.PoolBO;
import com.lapool.bo.UserExtBO;
import com.lapool.client.UserRestClient;
import com.lapool.dao.PoolExtDAO;
import com.lapool.dao.UserDAO;
import com.lapool.data.MutualFriendsBean;
import com.lapool.data.fb.FBLoginBean;
import com.lapool.data.UserBean;
import com.lapool.data.UserCurrentStatus;
import com.lapool.data.fb.FbMutualFriendsObject;
import com.lapool.data.fb.FriendsData;
import com.lapool.exception.BaseException;
import com.lapool.model.AuthProvider;
import com.lapool.model.FbUserLogin;
import com.lapool.model.PickUpRequest;
import com.lapool.model.Trip;
import com.lapool.util.ApplicationConstants;
import com.lapool.util.SystemUtils;
import com.owlike.genson.Genson;
import java.security.SignatureException;
import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author akshat666
 */
public class UserExtBOimpl implements UserExtBO {

    final static Logger log = LoggerFactory.getLogger(UserExtBOimpl.class);

    private UserDAO userDAO;

    private PoolExtDAO poolExtDAO;
    private PoolBO poolBO;
    private String appSecret;
    private UserRestClient userRestClient;

    public void setUserRestClient(UserRestClient userRestClient) {
        this.userRestClient = userRestClient;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public void setPoolBO(PoolBO poolBO) {
        this.poolBO = poolBO;
    }

    public void setPoolExtDAO(PoolExtDAO poolExtDAO) {
        this.poolExtDAO = poolExtDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional(rollbackFor = BaseException.class)
    public UserBean updateFBUserToken(long authID, FBLoginBean fBLoginBean) throws BaseException {
        final String methodName = "updateFBUserToken()";
        log.info("Entering :" + methodName);

        AuthProvider authProvider = userDAO.getAuthProvider(fBLoginBean.getUserID(), ApplicationConstants.PROVIDER_TYPE_FACEBOOK);
        UserBean userBean = new UserBean();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        //If change in FB access token
        if (!authProvider.getFbUserLogin().getToken().equals(fBLoginBean.getToken())) {
            authProvider.getFbUserLogin().setToken(fBLoginBean.getToken());
            authProvider.getFbUserLogin().setExpires(new Timestamp(fBLoginBean.getExpires()));
            authProvider.getFbUserLogin().setLastRefresh(new Timestamp(fBLoginBean.getLastRefresh()));
            authProvider.getFbUserLogin().setUpdated(currentTime);
        }

        userBean.setAuthID(authProvider.getId());
        userBean.setToken(authProvider.getFbUserLogin().getToken());

        log.info("Exiting :" + methodName);
        return userBean;
    }

    @Override
    @Transactional
    public UserCurrentStatus fetchUserCurrentState(long authID) throws BaseException {
        final String methodName = "fetchUserCurrentState()";
        log.info("Entering :" + methodName);

        Trip tripAsDropper = null;
        Trip tripAsHopper = null;
        UserCurrentStatus currentStatus = null;

        AuthProvider authProvider = userDAO.fetchAuthProvider(authID);
        if (!(authProvider.getStatus() == ApplicationConstants.STATUS_AUTH_USER_ACTIVE)) {
            currentStatus = new UserCurrentStatus();
            currentStatus.setUserStatus(authProvider.getStatus());
            return currentStatus;
        }

        PickUpRequest pickUpRequest = poolExtDAO.fetchLastPickupRequest(authID);

        //Check as a hopper
        if (pickUpRequest != null) {

            switch (pickUpRequest.getStatus()) {
                case ApplicationConstants.STATUS_PICKREQ_ACCEPTED:
                    tripAsHopper = pickUpRequest.getTrip();
                    if (tripAsHopper != null && tripAsHopper.getStatus() == ApplicationConstants.STATUS_TRIP_CREATED) {
                        currentStatus = new UserCurrentStatus();
                        currentStatus.setTripID(tripAsHopper.getTripID());
                        currentStatus.setTripStatus(tripAsHopper.getStatus());
                    }
                    break;
                case ApplicationConstants.STATUS_PICKREQ_NEW:
                    if ((System.currentTimeMillis() - pickUpRequest.getCreated().getTime()) <= ApplicationConstants.PICKUPREQ_EXPIRE_TIME_INTERVAL) {
                        currentStatus = new UserCurrentStatus();
                        currentStatus.setPickReqID(pickUpRequest.getPickID());
                        currentStatus.setPickReqStatus(pickUpRequest.getStatus());
                    }
                    break;

            }

        }

        //If not as a hopper, check as a dropper
        if (currentStatus == null) {
            tripAsDropper = poolExtDAO.fetchLastTripAsDropper(authID);

            if (tripAsDropper != null && tripAsDropper.getStatus() == ApplicationConstants.STATUS_TRIP_CREATED) {
                currentStatus = new UserCurrentStatus();
                currentStatus.setTripID(tripAsDropper.getTripID());
                currentStatus.setTripStatus(tripAsDropper.getStatus());
            }

        }

        //If both the PikupReq and trip are running at the same time - Cancel the pickupReq
        if (pickUpRequest != null
                && pickUpRequest.getStatus() == ApplicationConstants.STATUS_PICKREQ_NEW
                && ((System.currentTimeMillis() - pickUpRequest.getCreated().getTime()) > ApplicationConstants.PICKUPREQ_EXPIRE_TIME_INTERVAL)
                && tripAsDropper != null
                && tripAsDropper.getStatus() == ApplicationConstants.STATUS_TRIP_CREATED) {

            poolBO.changePickupRequestStatus(pickUpRequest.getPickID(), ApplicationConstants.STATUS_PICKREQ_CANCELLED);
            currentStatus = new UserCurrentStatus();
            currentStatus.setTripID(tripAsDropper.getTripID());
            currentStatus.setTripStatus(tripAsDropper.getStatus());
        }

        log.info("Exiting :" + methodName);
        return currentStatus;
    }

    @Override
    @Transactional
    public MutualFriendsBean fetchMutualConnections(long authIDone, long authIDtwo) throws BaseException {
        final String methodName = "fetchMutualConnections()";
        log.info("Entering :" + methodName);

        FbMutualFriendsObject fbMutualData = null;
        
        AuthProvider authProvider1 = userDAO.fetchAuthProvider(authIDone);
        AuthProvider authProvider2 = userDAO.fetchAuthProvider(authIDtwo);

        //Fetch FB mutual connections
        FbUserLogin fbUser1 = authProvider1.getFbUserLogin();
        MutualFriendsBean mutualFriendsBean = new MutualFriendsBean();

        if (fbUser1 != null) {

            String appSecretProof = null;

            try {
                appSecretProof = SystemUtils.hashMac(fbUser1.getToken(), appSecret);
            } catch (SignatureException ex) {
                throw new BaseException();
            }

            String jsonResult = userRestClient.fetchFbAllMutualFriends(fbUser1.getToken(), appSecretProof, authProvider2.getProviderId());
            Genson gen = new Genson();
            fbMutualData = gen.deserializeInto(jsonResult, new FbMutualFriendsObject());

            FriendsData[] userData = fbMutualData.getContext().getAll_mutual_friends().getData();
            
            mutualFriendsBean.setTotalMutualFriends(Long.valueOf(fbMutualData.getContext().getAll_mutual_friends().getSummary().getTotal_count()));

            for (FriendsData userData1 : userData) {
                MutualFriendsBean.User user = new MutualFriendsBean.User();
                user.name = userData1.getName();
                user.picUrl = userData1.getPicture().getData().getUrl();
                mutualFriendsBean.getUser().add(user);
            }
        }

        log.info("Exiting :" + methodName);
        return mutualFriendsBean;
    }

}
