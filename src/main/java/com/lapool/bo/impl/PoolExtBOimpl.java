/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bo.impl;

import com.lapool.bo.PoolExtBO;
import com.lapool.bo.PushBO;
import com.lapool.dao.PoolDAO;
import com.lapool.dao.PoolExtDAO;
import com.lapool.dao.UserDAO;
import com.lapool.data.AlertDetails;
import com.lapool.data.LocationDetails;
import com.lapool.data.PathBean;
import com.lapool.data.PickUpRequestBean;
import com.lapool.data.TripDetails;
import com.lapool.exception.BaseException;
import com.lapool.model.Alert;
import com.lapool.model.AlertLocation;
import com.lapool.model.AuthProvider;
import com.lapool.model.Path;
import com.lapool.model.PickUpRequest;
import com.lapool.util.ApplicationConstants;
import com.lapool.util.SystemUtils;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 *
 * @author akshat666
 */
public class PoolExtBOimpl implements PoolExtBO {

    final static Logger log = LoggerFactory.getLogger(PoolExtBOimpl.class);

    private PoolDAO poolDAO;
    private UserDAO userDAO;
    private PoolExtDAO poolExtDAO;
    private PushBO pushBO;

    public void setPushBO(PushBO pushBO) {
        this.pushBO = pushBO;
    }

    public void setPoolExtDAO(PoolExtDAO poolExtDAO) {
        this.poolExtDAO = poolExtDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setPoolDAO(PoolDAO poolDAO) {
        this.poolDAO = poolDAO;
    }

    @Override
    @Transactional
    public AlertDetails fetchAlertDetailsFromKey(String key) throws BaseException {
        final String methodName = "fetchAlertDetailsFromKey()";
        log.info("Entering :" + methodName + " - Key: " + key);

        AlertDetails alertDetails = new AlertDetails();
        TripDetails tripDetails = null;

        Alert alert = poolDAO.fetchAlertDetals(key);

        if (alert.getTrip() != null) {
            tripDetails = SystemUtils.copyTripToTripDetails(alert.getTrip());
            alertDetails.setTripDetails(tripDetails);
        }
        ArrayList<LocationDetails> alertLocations = new ArrayList<LocationDetails>();

        //Populate alert locations
        for (AlertLocation loc : alert.getAlertLocations()) {

            LocationDetails lp = new LocationDetails();
            lp.setLat(loc.getLat());
            lp.setLng(loc.getLng());
            lp.setCreated(loc.getCreated());
            alertLocations.add(lp);
        }
        alertDetails.setAlertLocations(alertLocations);
        alertDetails.setAlerterAuthID(alert.getAuthProvider().getId());
        alertDetails.setAlertAtLat(alert.getLat());
        alertDetails.setAlertAtLng(alert.getLng());
        alertDetails.setAlertedAt(alert.getCreated());
        alertDetails.setAlertedByName(alert.getAuthProvider().getName());
        return alertDetails;
    }

    @Override
    @Transactional
    public PickUpRequestBean fetchPickRequestIfActive(long pickReqID) throws BaseException {
        final String methodName = "fetchPickRequestStatus()";
        log.info("Entering :" + methodName + " - pickReqID: " + pickReqID);

        PickUpRequestBean pickUpRequestBean = null;

        PickUpRequest pickUpRequest = poolDAO.getPickUpRequest(pickReqID);

        //If pickupReq is still active and not expired
        if (pickUpRequest.getStatus() == ApplicationConstants.STATUS_PICKREQ_NEW
                && ((System.currentTimeMillis() - pickUpRequest.getCreated().getTime()) < ApplicationConstants.PICKUPREQ_EXPIRE_TIME_INTERVAL)) {
            pickUpRequestBean = new PickUpRequestBean();
            pickUpRequestBean.setPickID(pickUpRequest.getPickID());
            pickUpRequestBean.setPickAddress(pickUpRequest.getPickAddress());
            pickUpRequestBean.setPickUpLat(pickUpRequest.getPickUpLat());
            pickUpRequestBean.setPickUpLng(pickUpRequest.getPickUpLng());
            pickUpRequestBean.setDropAddress(pickUpRequest.getDropAddress());
            pickUpRequestBean.setDropLat(pickUpRequest.getDropLat());
            pickUpRequestBean.setDropLng(pickUpRequest.getDropLng());
            pickUpRequestBean.setMusic(pickUpRequest.getMusic() == 1);
            pickUpRequestBean.setGirlsOnly(pickUpRequest.getGirlsOnly() == 1);
            pickUpRequestBean.setSmoking(pickUpRequest.getSmoking() == 1);
            pickUpRequestBean.setOpenToAll(pickUpRequest.getOpenToAll() == 1);
            pickUpRequestBean.setCreated(pickUpRequest.getCreated());
        }

        log.info("Exiting :" + methodName);
        return pickUpRequestBean;
    }

    @Override
    @Transactional
    public void saveUserPath(long authID, PathBean pathBean) throws BaseException {
        final String methodName = "saveUserPath()";
        log.info("Entering :" + methodName + " - authID: " + authID);

        Timestamp now = new Timestamp(System.currentTimeMillis());

        //Validate the inputs
        if (pathBean == null
                || pathBean.getStartLat() <= 0
                || pathBean.getStartLng() <= 0
                || pathBean.getDestLat() <= 0
                || pathBean.getDestLng() <= 0
                || StringUtils.isEmpty(pathBean.getStartAddress())
                || StringUtils.isEmpty(pathBean.getDestAddress())
                || pathBean.getTime() <= 0) {

            log.error("Invalid inputs");
            throw new BaseException("Invalid inputs");

        }

        //Check if total active paths are less than equal to 4
        long totalPaths = poolExtDAO.totalPathsForAuthID(authID);
        if (totalPaths >= 4) {
            log.error("Only 4 Paths allowed per user : count" + totalPaths);
            throw new BaseException(ApplicationConstants.GENERIC_SERVER_ERROR, ApplicationConstants.ERROR_CODE_PATHS_EXCEEDED_LIMIT, methodName, methodName, methodName);
        }


        //Convert the time and DAYs to UTC time and DAYs
        Calendar calLocal = Calendar.getInstance(TimeZone.getTimeZone(pathBean.getTimeZoneID()));
        calLocal.setTimeInMillis(pathBean.getTime());

        //Have to convert to UTC time and check if Days of the Week has changed and set it
        // We are guessing the JVM is set to UTC timezone
        Calendar calUTC = Calendar.getInstance(TimeZone.getDefault());

        Path path = new Path();
        ArrayList<Integer> dayOfTheWeek = new ArrayList();

        if (pathBean.isSun()) {
            calLocal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            calUTC.setTimeInMillis(calLocal.getTimeInMillis());
            dayOfTheWeek.add(calUTC.get(Calendar.DAY_OF_WEEK));
        }
        if (pathBean.isMon()) {
            calLocal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            calUTC.setTimeInMillis(calLocal.getTimeInMillis());
            dayOfTheWeek.add(calUTC.get(Calendar.DAY_OF_WEEK));
        }
        if (pathBean.isTue()) {
            calLocal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            calUTC.setTimeInMillis(calLocal.getTimeInMillis());
            dayOfTheWeek.add(calUTC.get(Calendar.DAY_OF_WEEK));
        }
        if (pathBean.isWed()) {
            calLocal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            calUTC.setTimeInMillis(calLocal.getTimeInMillis());
            dayOfTheWeek.add(calUTC.get(Calendar.DAY_OF_WEEK));
        }
        if (pathBean.isThu()) {
            calLocal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            calUTC.setTimeInMillis(calLocal.getTimeInMillis());
            dayOfTheWeek.add(calUTC.get(Calendar.DAY_OF_WEEK));
        }
        if (pathBean.isFri()) {
            calLocal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            calUTC.setTimeInMillis(calLocal.getTimeInMillis());
            dayOfTheWeek.add(calUTC.get(Calendar.DAY_OF_WEEK));
        }
        if (pathBean.isSat()) {
            calLocal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            calUTC.setTimeInMillis(calLocal.getTimeInMillis());
            dayOfTheWeek.add(calUTC.get(Calendar.DAY_OF_WEEK));
        }

        for(int day :dayOfTheWeek){
            if(day == 1){
                path.setSun(1);
            }
            if(day == 2){
                path.setMon(1);
            }
            if(day == 3){
                path.setTue(1);
            }
            if(day == 4){
                path.setWed(1);
            }
            if(day == 5){
                path.setThu(1);
            }
            if(day == 6){
                path.setFri(1);
            }
            if(day == 7){
                path.setSat(1);
            }
        }

        
        AuthProvider authProvider = userDAO.fetchAuthProvider(authID);
        
        path.setAuthProvider(authProvider);
        path.setStartAddress(pathBean.getStartAddress());
        path.setStartLat(pathBean.getStartLat());
        path.setStartLng(pathBean.getStartLng());
        
        path.setStartRadiansLng(Math.toRadians(pathBean.getStartLng()));
        path.setStartSinRadiansLat(Math.sin(Math.toRadians(pathBean.getStartLat())));
        path.setStartCosRadiansLat(Math.cos(Math.toRadians(pathBean.getStartLat())));
        
        path.setDestAddress(pathBean.getDestAddress());
        path.setDestLat(pathBean.getDestLat());
        path.setDestLng(pathBean.getStartLng());
        
        path.setDestRadiansLng(Math.toRadians(pathBean.getDestLng()));
        path.setDestSinRadiansLat(Math.sin(Math.toRadians(pathBean.getDestLat())));
        path.setDestCosRadiansLat(Math.cos(Math.toRadians(pathBean.getDestLat())));
        
        path.setStartTime(new Time(calUTC.getTimeInMillis()));
        path.setStatus(ApplicationConstants.STATUS_PATH_ACTIVE);
        path.setCreated(now);
        path.setUpdated(now);

        poolExtDAO.savePath(path);

        log.info("Exiting :" + methodName);

    }

    
    @Override
    public void notifyMatchedPathUsers(PickUpRequestBean pickRequest, List alreadyNotifiedUsers, AuthProvider authProvider, long pickupID) throws BaseException {
        final String methodName = "notifyMatchedPathUsers()";
        log.info("Entering :" + methodName);

        //Fetch all userIDs who have thier PATHs matched with this pickReq
        //Range is set to 2km for start point and 2km for destination point
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);

        List authIDs = poolExtDAO.fetchUsersOnPath(pickRequest.getPickUpLat(), pickRequest.getPickUpLng(), pickRequest.getDropLat(), pickRequest.getDropLng(), 2, day_of_week);
        if(alreadyNotifiedUsers != null && alreadyNotifiedUsers.size() >0 && authIDs != null && authIDs.size() > 0){
            authIDs.removeAll(alreadyNotifiedUsers);
        }
        
        if(authIDs != null && authIDs.size() > 0)
        {
            pushBO.hopNotificationToListOfAuthIDs(authIDs, pickupID, authProvider.getName(), pickRequest.getDropAddress());
        }
        
        log.info("Exiting :" + methodName);
    }

    @Override
    @Transactional
    public List fetchActivePathList(long authID) throws BaseException {
        final String methodName = "fetchActivePathList()";
        log.info("Entering and exiting:" + methodName);
        
        List<com.lapool.model.Path> paths = poolExtDAO.fetchActivePaths(authID);
        List<PathBean> resultList = new ArrayList<PathBean>();
        
        for(com.lapool.model.Path path : paths){
            PathBean pathBean = new PathBean();
            pathBean.setId(path.getId());
            pathBean.setStartAddress(path.getStartAddress());
            pathBean.setDestAddress(path.getDestAddress());
            pathBean.setTime(path.getStartTime().getTime());
            pathBean.setMon(path.getMon() == 1);
            pathBean.setTue(path.getTue()== 1);
            pathBean.setWed(path.getWed()== 1);
            pathBean.setThu(path.getThu()== 1);
            pathBean.setFri(path.getFri()== 1);
            pathBean.setSat(path.getSat()== 1);
            pathBean.setSun(path.getSun()== 1);
            resultList.add(pathBean);
        }
        
        return resultList;
        
    }

    @Override
    @Transactional
    public void deleteUserPath(long pathID) throws BaseException {
        final String methodName = "deleteUserPath()";
        log.info("Entering: " + methodName);
        
        Path path = poolExtDAO.fetchUserPath(pathID);
        path.setStatus(ApplicationConstants.STATUS_PATH_INACTIVE);
        path.setUpdated(new Timestamp(System.currentTimeMillis()));
        
        log.info("Exiting : " + methodName);

    }

}
