/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.dao;

import com.lapool.data.LocationPoint;
import com.lapool.exception.BaseException;
import com.lapool.model.Alert;
import com.lapool.model.AlertLocation;
import com.lapool.model.PickUpRequest;
import com.lapool.model.Trip;
import com.lapool.model.TripLocation;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 *
 * @author akshat
 */
public interface PoolDAO {

    public long registerPickupRequest(PickUpRequest pickReq) throws BaseException;

    public List getPickupRequests(Set<Long> authIds, Timestamp fromCreatedTime, int status, LocationPoint p1, LocationPoint p2, LocationPoint p3, LocationPoint p4, int pageNumber, int pageSize, boolean listFemaleReqs) throws BaseException;

    public long saveTrip(Trip trip) throws BaseException;

    public PickUpRequest getPickUpRequest(long pickID) throws BaseException;

    public Trip getTripDetails(long tripID) throws BaseException;

    public long saveUpdatePickupRequest(PickUpRequest pickupReq) throws BaseException;

    /**
     * Updates the user location during a running trip
     *
     * @param location
     * @param authID
     * @param tripID
     * @return ID
     * @throws com.lapool.exception.BaseException
     */
    public long updateUserTripLocation(TripLocation location, long authID, long tripID) throws BaseException;

    /**
     * Check if the generated trip Key is unique
     *
     * @param tripKey
     * @return boolean
     * @throws BaseException
     */
    public boolean isTripKeyExist(String tripKey) throws BaseException;

    /**
     * Returns Trip details by passing the trip key
     *
     * @param key
     * @return Trip
     * @throws BaseException
     */
    public Trip fetchTripDetails(String key) throws BaseException;

    /**
     * Fetches all the tripLocations for a given trip
     *
     * @param tripID
     * @return
     * @throws BaseException
     */
    public List<TripLocation> fetchTripLocations(long tripID) throws BaseException;

    /**
     * Save user triggered alert details
     *
     * @param alert
     * @param tripID
     * @param authID
     * @return
     * @returnlong
     * @throws com.lapool.exception.BaseException
     */
    public long saveAlertDetails(Alert alert, long tripID, long authID) throws BaseException;

    public long saveAlertLocation(AlertLocation alertLocation, long alertID) throws BaseException;

    /**
     * Returns all the pickupRequests around the specified location
     *
     * @param authID
     * @param minusTime
     * @param status
     * @param p1
     * @param p2
     * @param p3
     * @param p4
     * @param pageNumber
     * @param pageSize
     * @param listFemalePickReqs
     * @return List
     * @throws com.lapool.exception.BaseException
     */
    public List fetchPublicPickRequests(long authID, Timestamp minusTime, int status, LocationPoint p1, LocationPoint p2, LocationPoint p3, LocationPoint p4, int pageNumber, int pageSize, boolean listFemalePickReqs) throws BaseException;

    /**
     * Fetch the Alert based on the key provided
     * @param key
     * @return
     * @throws BaseException 
     */
    public Alert fetchAlertDetals(String key) throws BaseException;

}
