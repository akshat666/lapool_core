/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bo;

import com.lapool.data.AlertDataBean;
import com.lapool.data.ChangeStatusBean;
import com.lapool.data.HopResult;
import com.lapool.data.LocationPoint;
import com.lapool.data.PickUpRequestBean;
import com.lapool.data.TripBean;
import com.lapool.data.TripDetails;
import com.lapool.exception.BaseException;
import java.util.List;

/**
 *
 * @author akshat
 */
public interface PoolBO {
    
    public HopResult registerPickUpRequest(PickUpRequestBean pickUpRequestBean) throws BaseException;
    
    public List getPickRequestsForAuthID(long authID, double srcLat, double srcLng, int pageNumber, int pageSize) throws BaseException;
    
    public long confirmPickupRequest(long authID, long tripID, double dropperLat, double dropperLng) throws BaseException;
    
    public TripBean getTripDetails(long tripID) throws BaseException;
    
    public long changePickupRequestStatus(long pickupID, int cancalType) throws BaseException;
    
    public long endTrip(ChangeStatusBean tripCancelBean) throws BaseException;

    /**
     * Updates the user location during a running trip
     * @param tripID
     * @param authID
     * @param location 
     * @throws com.lapool.exception.BaseException 
     */
    public void updateUserTripLocation(long tripID, long authID, LocationPoint location) throws BaseException;

    /**
     * Fetches the Trip details using the tripKey passed
     * @param key
     * @return Trip
     * @throws BaseException 
     */
    public TripDetails fetchTripDetailsFromKey(String key) throws BaseException;

    /**
     * Fetches the trip's locations polled back to the system by users
     * @param tripID
     * @param hopperID
     * @param dropperID
     * @return List
     * @throws BaseException 
     */
    public TripDetails fetchTripDetails(long tripID, long hopperID, long dropperID) throws BaseException;

    /**
     * Save the user triggered alert/SOS details
     * @param alertDataBean
     * @return 
     * @throws BaseException 
     */
    public AlertDataBean saveAlertDetails(AlertDataBean alertDataBean) throws BaseException;

    /**
     * Updates of the user location after triggering an alert
     * @param alertID
     * @param location
     * @throws BaseException 
     */
    public void updateAlertLocation(long alertID, LocationPoint location) throws BaseException;

    /**
     * 
     * @param authID
     * @param lat
     * @param lng
     * @param pageNumber
     * @param pageSize
     * @return List
     * @throws BaseException 
     */
    public List fetchPublicPickRequestsForAuthID(long authID, double lat, double lng, int pageNumber, int pageSize) throws BaseException;
 
}
