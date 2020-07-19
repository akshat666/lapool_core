/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bo;

import com.lapool.data.AlertDetails;
import com.lapool.data.PathBean;
import com.lapool.data.PickUpRequestBean;
import com.lapool.exception.BaseException;
import com.lapool.model.AuthProvider;
import java.util.List;

/**
 *
 * @author akshat666
 */
public interface PoolExtBO {
    
    public AlertDetails fetchAlertDetailsFromKey(String key) throws BaseException;
    
    public PickUpRequestBean fetchPickRequestIfActive(long pickReqID) throws BaseException; 
    
    /**
     *  Save the user fixed path to office/college etc
     * @param authID
     * @param path
     * @throws BaseException 
     */
    public void saveUserPath(long authID, PathBean path) throws BaseException;

    /**
     * 
     * @param pickRequest
     * @param alreadyNotifiedUsers
     * @param authProvider
     * @throws BaseException 
     */
    public void notifyMatchedPathUsers(PickUpRequestBean pickRequest, List alreadyNotifiedUsers, AuthProvider authProvider, long pickupID) throws BaseException;
    
    /**
     * Returns a list of active paths for the authID provided
     * @param authID
     * @return List
     * @throws BaseException 
     */
    public List fetchActivePathList(long authID) throws BaseException;

    /**
     * Deletes the user PATH identified by pathID
     * @param pathID
     * @throws BaseException 
     */
    public void deleteUserPath(long pathID) throws BaseException;
}
