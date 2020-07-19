/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.dao;

import com.lapool.exception.BaseException;
import com.lapool.model.Path;
import com.lapool.model.PickUpRequest;
import com.lapool.model.Trip;
import java.util.List;

/**
 *
 * @author akshat666
 */
public interface PoolExtDAO {
    
    /**
     *  Fetch the users last trip
     * @param authID
     * @return
     * @throws BaseException 
     */
    public Trip fetchLastTripAsDropper(long authID) throws BaseException;
    
    
    /**
     * Fetch the user's last pickupReq
     * @param authID
     * @return
     * @throws BaseException 
     */
    public PickUpRequest fetchLastPickupRequest(long authID) throws BaseException;
    
    
    /**
     * Fetches the total number of Paths created for user
     * @param authID
     * @return Total no of Paths
     * @throws BaseException 
     */
    public long totalPathsForAuthID(long authID) throws BaseException;

    /**
     * Save the Path
     * @param path
     * @return long - pathID
     * @throws BaseException 
     */
    public long savePath(Path path) throws BaseException;

    /**
     * MEthod returns all users whose PATHs match the pickUpReq parameters and range
     * @param pickUpLat
     * @param pickUpLng
     * @param dropLat
     * @param dropLng
     * @param i - range in km
     * @return List - authIDs
     * @throws BaseException 
     */
    public List fetchUsersOnPath(double pickUpLat, double pickUpLng, double dropLat, double dropLng, double distance, int day_of_week) throws BaseException;

    /**
     * Returns a list of active PATHs for the authID
     * @param authID
     * @return List
     * @throws BaseException 
     */
    public List fetchActivePaths(long authID) throws BaseException;

    /**
     * Deletes the path(logically) identified by the pathID
     * @param pathID
     * @return Path
     * @throws BaseException 
     */
    public Path fetchUserPath(long pathID) throws BaseException;
    
    
    
}
