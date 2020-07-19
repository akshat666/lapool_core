package com.lapool.dao;

import com.lapool.exception.BaseException;
import com.lapool.model.AuthProvider;
import com.lapool.model.Location;
import com.lapool.model.Message;
import com.lapool.model.SystemUser;
import com.lapool.model.Token;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.primefaces.model.SortOrder;

/**
 *
 * @author akshat
 */

public interface UserDAO {
    
    public long saveFbLoginDetails(AuthProvider authProvider) throws BaseException;
    
    public boolean isUserIDExists(String userID, int providerType) throws BaseException;
    
    //public long fetchAuthProviderID(long userID, int providerType) throws BaseException;
    
    //public void saveWorkDetails(WorkDetails workDetails) throws BaseException;

    public boolean isUserChannelKeyPresent(String key) throws BaseException;
    
    public void updateKeyInUserChannel(String oldKey, String newKey) throws BaseException;
    
    public Object[] getChannelAndAuthID(String key) throws BaseException;

    //public boolean isEmailPresent(String email) throws BaseException;

    //public String getVerificationKeyFromEmail(String email) throws BaseException;

    public void updateAuthProvider(AuthProvider authProvider) throws BaseException;
    
    public AuthProvider getAuthProvider(String userID, int providerType) throws BaseException;
    
    public AuthProvider fetchAuthProvider(long authID) throws BaseException;

    public List getListOfActiveAuthIds(List userIds, int providerType) throws BaseException;
    
    public void saveUpdateLocation(Location location) throws BaseException;

    public long saveMessage(Message message) throws BaseException;
    
    public List getNewMessage(long tripID, long msgID, long chatAuthID) throws BaseException;

    public long saveToken(Token token) throws BaseException;

    public Token fetchToken(long id) throws BaseException;

    public void updateToken(Token token) throws BaseException;
    
    public List fetchUsersByDistance(double lat, double lng, Collection ids, double distance) throws BaseException;
    
    /**
     *  Returns the chat message history for a trip before the msgID message
     * @param tripID
     * @param msgID
     * @return List
     * @throws BaseException 
     */
    public List getMessageHistory(long tripID, long msgID) throws BaseException;
    
    public SystemUser fetchSystemUser(long accountID, String userName) throws BaseException;

    /**
     * Returns the total number of System users applying the filter
     * @param filters
     * @return long
     * @throws BaseException 
     */
    public long fetchSystemUserCount(Map<String, Object> filters) throws BaseException;

    /**
     *  Returns the System users
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @param filters
     * @return List
     * @throws com.lapool.exception.BaseException
     */
    public List fetchSystemUsers(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) throws BaseException;

    /**
     * Returns the current hibernate session
     * @return 
     */
    public Session getCurrentSession();
}
