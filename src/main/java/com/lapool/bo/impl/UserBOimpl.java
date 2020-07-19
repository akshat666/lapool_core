package com.lapool.bo.impl;

import com.lapool.bo.UserBO;
import com.lapool.client.OneSignalRESTClient;
import com.lapool.client.UserRestClient;
import com.lapool.dao.PoolDAO;
import com.lapool.dao.UserDAO;
import com.lapool.data.fb.FBLoginBean;
import com.lapool.data.MessageBean;
import com.lapool.data.PushNotification;
import com.lapool.data.UserBean;
import com.lapool.exception.BaseException;
import com.lapool.model.Account;
import com.lapool.model.AuthProvider;
import com.lapool.model.FbUserLogin;
import com.lapool.model.Location;
import com.lapool.model.Message;
import com.lapool.model.Privilege;
import com.lapool.model.SystemUser;
import com.lapool.model.Token;
import com.lapool.model.Trip;
import com.lapool.util.ApplicationConstants;
import com.lapool.util.SystemUtils;
import com.owlike.genson.Genson;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author akshat
 */
public class UserBOimpl implements UserBO {

    final static Logger log = LoggerFactory.getLogger(UserBOimpl.class);

    private UserDAO userDAO;
    
    private PoolDAO poolDAO;

    public void setPoolDAO(PoolDAO poolDAO) {
        this.poolDAO = poolDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private UserRestClient userRestClient;

    public void setUserRestClient(UserRestClient userRestClient) {
        this.userRestClient = userRestClient;
    }

    private String oneSignalAppID;

    public void setOneSignalAppID(String oneSignalAppID) {
        this.oneSignalAppID = oneSignalAppID;
    }

    private OneSignalRESTClient oneSignalRESTClient;

    public void setOneSignalRESTClient(OneSignalRESTClient oneSignalRESTClient) {
        this.oneSignalRESTClient = oneSignalRESTClient;
    }

    @Override
    @Transactional
    public UserBean saveFBUserLoginDetails(FBLoginBean fBLoginBean) throws BaseException {
        long id = 0;

        final String methodName = "saveFBUserLoginDetails()";
        log.info("Entering :" + methodName);

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        // Check if FB user logged-in is an authentic user
        if (!this.isAuthorizedFBUser(fBLoginBean)) {
            throw new BaseException("Error in user authentication. Re-login again!");
        }
        UserBean userBean = new UserBean();
//        net.sf.ehcache.CacheManager cacheMgr = cacheManager.getCacheManager();
//        Cache cache = cacheMgr.getCache(ApplicationConstants.TOKEN);

        // Check if user already in system and save details
        // If user present skip the saving details part
        if (!this.userDAO.isUserIDExists(fBLoginBean.getUserID(), ApplicationConstants.PROVIDER_TYPE_FACEBOOK)) {

            //Save user login details
            FbUserLogin fbUserLogin = new FbUserLogin();
            fbUserLogin.setApplicationID(fBLoginBean.getApplicationID());
            fbUserLogin.setLastRefresh(new Timestamp(fBLoginBean.getLastRefresh()));
            fbUserLogin.setCreated(new Timestamp(System.currentTimeMillis()));
            fbUserLogin.setUpdated(new Timestamp(System.currentTimeMillis()));
            fbUserLogin.setSource(fBLoginBean.getSource());
            fbUserLogin.setToken(fBLoginBean.getToken());
            fbUserLogin.setExpires(new Timestamp(fBLoginBean.getExpires()));

            AuthProvider authProvider = new AuthProvider();
            authProvider.setCreated(currentTime);
            authProvider.setUpdated(currentTime);
            authProvider.setProviderId(fBLoginBean.getUserID());
            authProvider.setProviderType(ApplicationConstants.PROVIDER_TYPE_FACEBOOK);
            authProvider.setName(fBLoginBean.getName());
            authProvider.setEmail(fBLoginBean.getEmail());
            authProvider.setGender(fBLoginBean.getGender());
            authProvider.setPhone(fBLoginBean.getPhone());
            authProvider.setStatus(ApplicationConstants.STATUS_AUTH_USER_ACTIVE);
            authProvider.setBirthday(new Date(fBLoginBean.getBirthday()));

            authProvider.setFbUserLogin(fbUserLogin);
            fbUserLogin.setAuthProvider(authProvider);

            id = this.userDAO.saveFbLoginDetails(authProvider);

            Token token = new Token();
            token.setAuthProvider(authProvider);
            token.setToken(SystemUtils.generateUniqueID());
            token.setCreated(currentTime);
            token.setUpdated(currentTime);

            userDAO.saveToken(token);

            userBean.setAuthID(id);
            userBean.setToken(token.getToken());

            //Send confirmation email
        }

        log.info("Exiting :" + methodName);
        return userBean;
    }

    public boolean isAuthorizedFBUser(FBLoginBean fBLoginBean) throws BaseException {
        final String methodName = "isAuthorizedFBUser";
        log.info("Entering :" + methodName);

        String result = (String) this.userRestClient.callFBGraphME(fBLoginBean.getToken());
        Map<String, String> map = SystemUtils.convertJSONRStringToMap(result);

        //Check userid if it is same person
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equals(ApplicationConstants.ID) && entry.getValue().equals(fBLoginBean.getUserID())) {
                log.info("Exiting :" + methodName);
                return true;
            }
        }
        log.info("Exiting :" + methodName + " : Not authorized FB user");
        return false;
    }

    @Override
    @Transactional
    public boolean isValidKey(String key) throws BaseException {

        final String methodName = "isValidKey()";
        log.info("Entering  :" + methodName);
        boolean result = false;

        // if key is present, set it to NULL
        if (userDAO.isUserChannelKeyPresent(key)) {

            // Clear the key and set it as NULL and make user active
            userDAO.updateKeyInUserChannel(key, null);
            result = true;
        }
        log.info("Exiting :" + methodName);
        return result;

    }

    @Override
    @Transactional
    public Object[] getChannelAndAuthID(String key) throws BaseException {
        final String methodName = "getAuthProvider()";
        log.info("Entering and exiting :" + methodName);

        return userDAO.getChannelAndAuthID(key);

    }

//    @Override
//     public void subscribeToChannel(long authID, String channel) throws BaseException {
//        final String methodName = "subscribeToChannel()";
//        log.info("Entering :" + methodName);
//        
//        ArrayList<String> channels = new ArrayList<String>();
//        channels.add(channel.replace(".", "_"));
//        
//        UpdateChannelsBean updateChannelsBean = new UpdateChannelsBean();
//        updateChannelsBean.setAuthID(authID);
//        updateChannelsBean.setChannels(
//                new ChannelsBean(ApplicationConstants.ADDUNIQUE,
//                        channels.toArray(new String[channels.size()]))
//        );
//        
//        Genson genson = new Genson();
//        String jsonUpdateChannels = genson.serialize(updateChannelsBean);
//        
//        parseRESTClient.callUpdateChannels(jsonUpdateChannels);
//
//        log.info("Exiting :" + methodName);
//    }
    @Override
    @Transactional
    public void updateUserLocation(long authID, com.lapool.data.LocationPoint locationPoint) throws BaseException {
        final String methodName = "updateUserLocation()";
        log.info("Entering :" + methodName + " authID:" + authID);
        Location location;

        if (authID == 0 || locationPoint.getLat() == 0 || locationPoint.getLng() == 0) {
            log.error(methodName + " Invalid inputs recieved");
            throw new BaseException("Invalid inputs for location update");
        }
        AuthProvider authProvider = userDAO.fetchAuthProvider(authID);
        if (authProvider != null) {
            if (authProvider.getLocation() != null) {
                location = authProvider.getLocation();
            } else {
                location = new Location();
                location.setCreated(new Timestamp(System.currentTimeMillis()));
            }

            location.setLastLat(locationPoint.getLat());
            location.setLastLng(locationPoint.getLng());
            location.setRadiansLng(Math.toRadians(locationPoint.getLng()));
            location.setSinRadiansLat(Math.sin(Math.toRadians(locationPoint.getLat())));
            location.setCosRadiansLat(Math.cos(Math.toRadians(locationPoint.getLat())));

            location.setUpdated(new Timestamp(System.currentTimeMillis()));
            authProvider.setLocation(location);
            location.setAuthProvider(authProvider);

            userDAO.saveUpdateLocation(location);
        }
        log.info("Exiting :" + methodName);

    }

    @Override
    @Transactional
    public long saveMessage(MessageBean messageBean) throws BaseException {
        final String methodName = "sendMessage()";
        log.info("Entering :" + methodName);
        
        Trip currentTrip = poolDAO.getTripDetails(messageBean.getTripID());
        if(currentTrip.getStatus() != ApplicationConstants.STATUS_TRIP_CREATED){
            throw new BaseException(ApplicationConstants.GENERIC_SERVER_ERROR, ApplicationConstants.ERROR_CODE_TRIP_NOT_ACTIVE, 
                    ApplicationConstants.MSG_TRIP_NOT_ACTIVE, ApplicationConstants.MSG_TRIP_NOT_ACTIVE, null);
        }

        AuthProvider from = (AuthProvider) userDAO.getCurrentSession().load(AuthProvider.class,messageBean.getFrom());
        AuthProvider to = (AuthProvider) userDAO.getCurrentSession().load(AuthProvider.class,messageBean.getTo());
        to.setId(messageBean.getTo());
        Trip trip = new Trip();
        trip.setTripID(messageBean.getTripID());

        Message message = new Message();
        message.setFromUser(from);
        message.setToUser(to);
        message.setTrip(trip);
        message.setMessage(messageBean.getMessage());
        message.setCreated(new Timestamp(System.currentTimeMillis()));
        message.setUpdated(new Timestamp(System.currentTimeMillis()));

        // Send notification to the user to be picked 
        // This is specific to android devices
        //--------- start ------------
        PushNotification pushNotification = new PushNotification();
        pushNotification.setApp_id(this.oneSignalAppID);

        HashMap<String, Object> data = new HashMap<String, Object>();
        Genson gen = new Genson();

        data.put("data_type", ApplicationConstants.PUSH_TYPE_CHAT);
        data.put("tripID", messageBean.getTripID());

        pushNotification.setData(data);
//        HashMap<String, String> tags = new HashMap<String, String>();
//        tags.put("key", "authID");
//        tags.put("relation", "=");
//        tags.put("value", Long.toString(messageBean.getTo()));
//        ArrayList tagList = new ArrayList();
//        tagList.add(tags);

        HashMap<String, String> filters = new HashMap<String, String>();
        ArrayList filterList = new ArrayList();
        filters.put("field", "tag");
        filters.put("key", "authID");
        filters.put("relation", "=");
        filters.put("value", Long.toString(messageBean.getTo()));
        filterList.add(filters);

        HashMap<String, String> headings = new HashMap<String, String>();
        headings.put("en", from.getName());

        HashMap<String, String> contents = new HashMap<String, String>();
        contents.put("en", messageBean.getMessage());

        //pushNotification.setTags(tagList);
        pushNotification.setFilters(filterList);
        pushNotification.setHeadings(headings);
        pushNotification.setContents(contents);
        pushNotification.setAndroid_background_data(true);
        pushNotification.setAndroid_group(String.valueOf(ApplicationConstants.PUSH_TYPE_CHAT));
        pushNotification.setAndroid_group_message(ApplicationConstants.PUSH_GRP_MSG_CHAT);
        oneSignalRESTClient.pushNotification(gen.serialize(pushNotification));
        // ---------- stop ------------

        return userDAO.saveMessage(message);
    }

    @Override
    @Transactional
    public List<MessageBean> getNewMessage(long tripID, long msgID, long chatAuthID) throws BaseException {
        final String methodName = "getNewMessage()";
        log.info("Entering :" + methodName);

        if (tripID <= 0) {
            log.error("TripID is 0 :" + methodName);
            throw new BaseException("TripID was 0");
        }

        List result = null;

        //If no chatAuthID provided then return all chat history
        if (chatAuthID == -1) {
            List<Message> messages = userDAO.getMessageHistory(tripID, msgID);
            result = copyToMessageBean(messages);
        } else {
            List<Message> messages = userDAO.getNewMessage(tripID, msgID, chatAuthID);
            result = copyToMessageBean(messages);
        }

        log.info("Exiting :" + methodName);
        return result;

    }

    private List copyToMessageBean(List<Message> messages) {
        List<MessageBean> msgs = null;

        if (messages != null && !messages.isEmpty()) {
            msgs = new ArrayList<MessageBean>();
            for (Message msg : messages) {
                MessageBean messageBean = new MessageBean();
                messageBean.setId(msg.getId());
                messageBean.setFrom(msg.getFromUser().getId());
                messageBean.setTo(msg.getToUser().getId());
                messageBean.setMessage(msg.getMessage());

                msgs.add(messageBean);

            }
        }
        return msgs;
    }

    @Override
    @Transactional
    public boolean isTokenValid(long authID, String token) throws BaseException {
        final String methodName = "fetchToken()";
        log.info("Entering :" + methodName);

        Token t = userDAO.fetchToken(authID);
        if (t.getToken().equalsIgnoreCase(token)) {
            return true;
        }

        log.info("Exiting :" + methodName + " : Invalid Token for " + authID);
        return false;
    }

    @Override
    @Transactional
    public UserBean checkUserPresent(UserBean userBean) throws BaseException {
        final String methodName = "checkUserPresent()";
        String userID = userBean.getUserID();
        int providerType = userBean.getProviderType();
        log.info("Entering :" + methodName + " : Checking userID:" + userID + " and providerType:" + providerType);

        if (userID == null || providerType <= 0) {
            throw new BaseException("Null userID or providerType");
        }
        UserBean returnUserBean = new UserBean();

        if (userDAO.isUserIDExists(userID, providerType)) {
            log.info("UserID - " + userID + " already present in the system");
            AuthProvider authProvider = userDAO.getAuthProvider(userID, providerType);
            long id = authProvider.getId();
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            //User logged in again - update token to new value
            Token token = userDAO.fetchToken(authProvider.getId());
            token.setToken(SystemUtils.generateUniqueID());
            token.setUpdated(currentTime);
            
            //Update user social login token values
            if(providerType == ApplicationConstants.PROVIDER_TYPE_FACEBOOK){
                authProvider.getFbUserLogin().setToken(userBean.getFbLoginBean().getToken());
                authProvider.getFbUserLogin().setExpires(new Timestamp(userBean.getFbLoginBean().getExpires()));
                authProvider.getFbUserLogin().setApplicationID(userBean.getFbLoginBean().getApplicationID());
                authProvider.getFbUserLogin().setLastRefresh(new Timestamp(userBean.getFbLoginBean().getLastRefresh()));
                authProvider.getFbUserLogin().setSource(userBean.getFbLoginBean().getSource());
                authProvider.getFbUserLogin().setUpdated(currentTime);
            }

            returnUserBean.setAuthID(id);
            returnUserBean.setUserID(authProvider.getProviderId());
            returnUserBean.setToken(token.getToken());
            returnUserBean.setName(authProvider.getName());
            returnUserBean.setGender(authProvider.getGender());

        } //If user is a new registration set the authID as 0
        else {
            returnUserBean.setAuthID(0);
        }

        log.info("Exiting :" + methodName);
        return returnUserBean;
    }

    @Override
    @Transactional
    public SystemUser fetchSystemUser(long accountID, String userName) throws BaseException {
        final String methodName = "fetchSystemUser()";
        log.info("Entering and exiting :" + methodName + " : accountID:" + accountID + " and userName:" + userName);

        SystemUser user = userDAO.fetchSystemUser(accountID, userName);
        Account acc = user.getAccount();
        Set<Privilege> privileges  = acc.getPrivileges();
        acc.setPrivileges(SystemUtils.initializeAndUnproxy(privileges));
        user.setAccount(SystemUtils.initializeAndUnproxy(acc));
        
        return user;

    }

    @Override
    @Transactional
    public boolean isSystemUser(long account, String userName, String password) throws BaseException {
        final String methodName = "isSystemUser()";
        log.info("Entering :" + methodName);

        SystemUser user = userDAO.fetchSystemUser(account, userName);

        if (user != null && user.getPassword().equals(SystemUtils.hashPassword(password)) && user.getStatus() == ApplicationConstants.STATUS_SYSTEMUSER_ACTIVE) {
            return true;
        }

        log.info("Exiting :" + methodName);
        return false;
    }

    @Override
    @Transactional(rollbackFor = BaseException.class)
    public void changePassword(long accountID, String username, String oldPwd, String newPwd) throws BaseException {
        final String methodName = "changePassword()";
        log.info("Entering :" + methodName);

        SystemUser user = userDAO.fetchSystemUser(accountID, username);

        if (user == null) {
            throw new BaseException("No user found");
        }

        if (user.getPassword().equals(SystemUtils.hashPassword(oldPwd))) {
            user.setPassword(SystemUtils.hashPassword(newPwd));

        } else {
            throw new BaseException("Passwords do not match");
        }

        log.info("Exiting:" + methodName);
    }

    @Override
    @Transactional
    public long fetchSystemUserCount(Map<String, Object> filters) throws BaseException {
        final String methodName = "fetchSystemUserCount()";
        log.info("Entering and exiting:" + methodName);
        
        return userDAO.fetchSystemUserCount(filters);
    }

    @Override
    @Transactional
    public List fetchSystemUsers(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) throws BaseException {
        final String methodName = "fetchSystemUsers()";
        log.info("Entering and exiting:" + methodName);

        return userDAO.fetchSystemUsers(first, pageSize, sortField, sortOrder, filters);
    }

}
