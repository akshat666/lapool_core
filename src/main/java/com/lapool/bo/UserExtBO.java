/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bo;

import com.lapool.data.MutualFriendsBean;
import com.lapool.data.fb.FBLoginBean;
import com.lapool.data.UserBean;
import com.lapool.data.UserCurrentStatus;
import com.lapool.exception.BaseException;

/**
 *
 * @author akshat666
 */
public interface UserExtBO {
    
    /**
     *  Updates the User's Login changes like token, expiry time etc
     * @param authID
     * @param fBLoginBean
     * @return UserBean
     * @throws BaseException 
     */
    public UserBean updateFBUserToken(long authID, FBLoginBean fBLoginBean) throws BaseException;

    /**
     *  Gets the user's current state in the system - example: Trip is running OR PickupReq is running
     * @param authID
     * @return
     * @throws BaseException 
     */
    public UserCurrentStatus fetchUserCurrentState(long authID) throws BaseException;
    
    
    public MutualFriendsBean fetchMutualConnections(long authIDone, long authIDtwo) throws BaseException;

    
}
