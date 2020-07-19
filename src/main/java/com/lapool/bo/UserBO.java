/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bo;

import com.lapool.data.fb.FBLoginBean;
import com.lapool.data.MessageBean;
import com.lapool.data.UserBean;
import com.lapool.exception.BaseException;
import com.lapool.model.SystemUser;
import java.util.List;
import java.util.Map;
import org.primefaces.model.SortOrder;

/**
 *
 * @author akshat
 */
public interface UserBO {
    
    public UserBean saveFBUserLoginDetails(FBLoginBean fBLoginBean) throws BaseException;
    
    //public void verifyEmail(EmailVerificationBean emailVerificationBean) throws BaseException;
    
    public boolean isValidKey(String key) throws BaseException;
    
    public Object[] getChannelAndAuthID(String key) throws BaseException;

    /**
     * 
     * Method that updates periodically the user's location
     * @param authID
     * @param location
     * @throws BaseException 
     */
    public void updateUserLocation(long authID, com.lapool.data.LocationPoint location) throws BaseException;

    public long saveMessage(MessageBean message) throws BaseException;

    public List<MessageBean> getNewMessage(long tripID, long msgID, long chatAuthID) throws BaseException;

    public boolean isTokenValid(long authID, String token) throws BaseException;

    /**
     *  Checks if the new user logging in is already present in the system or not
     * @param userBean
     * @return boolean
     * @throws BaseException
     */
    public UserBean checkUserPresent(UserBean userBean) throws BaseException;
    
    /**
     * Fetch the System user 
     * @param accountID
     * @param userName
     * @return SystemUser
     * @throws BaseException 
     */
    public SystemUser fetchSystemUser(long accountID, String userName) throws BaseException;

    /**
     *  Check if the user is a part of System users
     * @param account
     * @param userName
     * @param password
     * @return
     * @throws BaseException 
     */
    public boolean isSystemUser(long account, String userName, String password) throws BaseException;

    /**
     * Change the current password to new password
     * @param accountID
     * @param username
     * @param oldPwd
     * @param newPwd 
     * @throws com.lapool.exception.BaseException 
     */
    public void changePassword(long accountID, String username, String oldPwd, String newPwd) throws BaseException;

    /**
     *  Return the count of total System users
     * @return
     * @throws BaseException 
     */
    public long fetchSystemUserCount(Map<String, Object> filters) throws BaseException;
    
    /**
     * Fetch system users via pagination
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @param filters
     * @return 
     * @throws BaseException 
     */
    public List fetchSystemUsers(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) throws BaseException;
}
