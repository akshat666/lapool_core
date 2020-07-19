package com.lapool.dao.impl;

import com.lapool.dao.UserDAO;
import com.lapool.exception.BaseException;
import com.lapool.model.AuthProvider;
import com.lapool.model.Location;
import com.lapool.model.Message;
import com.lapool.model.SystemUser;
import com.lapool.model.Token;
import com.lapool.model.UserChannel;
import com.lapool.util.ApplicationConstants;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author akshat
 */
@Repository
public class UserDAOimpl implements UserDAO {

    final static Logger log = LoggerFactory.getLogger(UserDAOimpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }

    @Override
    public long saveFbLoginDetails(AuthProvider authProvider) throws BaseException {

        final String methodName = "saveFbLoginDetails()";
        log.info("Entering :" + methodName);
        long id;
        try {
            id = (Long) this.sessionFactory.getCurrentSession().save(authProvider);
        } catch (HibernateException he) {
            log.error(he.getMessage(), he);
            throw new BaseException("Error saving Facebook user Login details");
        }
        log.info("Exiting :" + methodName);
        return id;
    }

    @Override
    public boolean isUserIDExists(String userID, int providerType) throws BaseException {
        final String methodName = "isUserIDExists()";
        log.info("Entering :" + methodName);

        Long resultLength = null;
        try {
            resultLength = (Long) this.sessionFactory.getCurrentSession()
                    .createQuery("select count(*) from AuthProvider authProvider where authProvider.providerId = :providerId and authProvider.providerType = :providerType")
                    .setParameter("providerId", userID)
                    .setParameter("providerType", providerType)
                    .uniqueResult();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error checking Facebook user details in the system");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage(), ex);
            throw new BaseException("Error checking Facebook user details");
        }
        log.info("Exiting :" + methodName);
        return resultLength > 0;
    }

//    @Override
//    public long fetchAuthProviderID(long userID, int providerType) throws BaseException {
//        final String methodName = "fetchAuthProvider()";
//        log.info("Entering :" + methodName);
//
//        List<Long> resultList = null;
//        try {
//            resultList = this.sessionFactory.getCurrentSession()
//                    .createQuery("select id from AuthProvider authProvider where authProvider.providerId = :userID and authProvider.providerType = :providerType")
//                    .setParameter("userID", Long.toString(userID))
//                    .setParameter("providerType", providerType).list();
//        } catch (HibernateException he) {
//            log.error("Hibernate Exception : " + he.getMessage());
//            throw new BaseException("Error fetching authorization provider");
//        } catch (Exception ex) {
//            log.error("Exception : " + ex.getMessage());
//            throw new BaseException("Error fetching authorization provider");
//        }
//        log.info("Exiting :" + methodName);
//        if (null == resultList.get(0)) {
//            return 0;
//        } else {
//            return resultList.get(0);
//        }
//
//    }
//    @Override
//    public void saveWorkDetails(WorkDetails workDetails) throws BaseException {
//        final String methodName = "saveWorkDetails()";
//        log.info("Entering :" + methodName);
//
//        try {
//            this.sessionFactory.getCurrentSession().save(workDetails);
//        } catch (HibernateException he) {
//            log.error("Hibernate Exception : " + he.getMessage());
//            throw new BaseException("Error saving Work Details");
//        } catch (Exception ex) {
//            log.error("Exception : " + ex.getMessage());
//            throw new BaseException("Error saving Work Details");
//        }
//        log.info("Exiting :" + methodName);
//    }
    @Override
    public boolean isUserChannelKeyPresent(String key) throws BaseException {
        final String methodName = "isWorkDetailsKeyPresent()";
        log.info("Entering :" + methodName);

        long resultLength;
        try {
            resultLength = (Long) this.sessionFactory.getCurrentSession()
                    .createQuery("select count(*) from UserChannel uc where uc.verificationKey = :key")
                    .setParameter("key", key)
                    .uniqueResult();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching User channel details");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage());
            throw new BaseException("Error fetching User channel details");
        }
        log.info("Exiting :" + methodName);
        return resultLength > 0;
    }

    @Override
    public void updateKeyInUserChannel(String oldKey, String newKey) throws BaseException {

        final String methodName = "updateKeyInWorkDetails()";
        log.info("Entering :" + methodName);

        UserChannel userChannel = null;
        try {
            userChannel = (UserChannel) this.sessionFactory.getCurrentSession()
                    .createQuery("from UserChannel uc where uc.verificationKey = :key")
                    .setParameter("key", oldKey)
                    .list().get(0);
            if (null != userChannel) {
                userChannel.setVerificationKey(newKey);
                userChannel.setStatus(ApplicationConstants.STATUS_USERCHANNEL_ACTIVE);
                this.sessionFactory.getCurrentSession().update(userChannel);
            }
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching Work details");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage());
            throw new BaseException("Error fetching Work details");
        }

        log.info("Exiting :" + methodName);

    }

    @Override
    public Object[] getChannelAndAuthID(String key) throws BaseException {
        final String methodName = "getAuthProvider()";
        log.info("Entering :" + methodName);

        List<Object[]> result = null;
        try {
            result = (List<Object[]>) this.sessionFactory.getCurrentSession()
                    .createQuery("select ap.id,wd.channel from AuthProvider ap join ap.workDetails wd where wd.verificationKey = :key")
                    .setParameter("key", key)
                    .list();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching channels and authID");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage());
            throw new BaseException("Error fetching AuthProvider");
        }
        log.info("Exiting :" + methodName);
        if (null == result || 0 >= result.size()) {
            return null;
        }
        return result.get(0);
    }

//    @Override
//    public boolean isEmailPresent(String email) throws BaseException {
//        final String methodName = "isEmailPresent()";
//        log.info("Entering :" + methodName);
//
//        long resultLength;
//        try {
//            resultLength = (Long) this.sessionFactory.getCurrentSession()
//                    .createQuery("select count(*) from WorkDetails wd where wd.email = :email")
//                    .setParameter("email", email)
//                    .uniqueResult();
//        } catch (HibernateException he) {
//            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
//            throw new BaseException("Error fetching Work details");
//        } catch (Exception ex) {
//            log.error("Exception : " + methodName + " :" + ex.getMessage());
//            throw new BaseException("Error fetching Work details");
//        }
//        log.info("Exiting :" + methodName);
//        return resultLength > 0;
//    }
//    @Override
//    public String getVerificationKeyFromEmail(String email) throws BaseException {
//
//        final String methodName = "getAuthProvider()";
//        log.info("Entering :" + methodName);
//
//        List<String> result = null;
//        try {
//            result = (List<String>) this.sessionFactory.getCurrentSession()
//                    .createQuery("select verificationKey from WorkDetails wd where wd.email = :email")
//                    .setParameter("email", email)
//                    .list();
//        } catch (HibernateException he) {
//            log.error("Hibernate Exception : " + he.getMessage());
//            throw new BaseException("Error fetching key");
//        } catch (Exception ex) {
//            log.error("Exception : " + ex.getMessage());
//            throw new BaseException("Error fetching key");
//        }
//        log.info("Exiting :" + methodName);
//        if (null == result) {
//            return null;
//        }
//        return result.get(0);
//
//    }
    @Override
    public void updateAuthProvider(AuthProvider authProvider) throws BaseException {

        final String methodName = "updateAuthProvider()";
        log.info("Entering :" + methodName);

        try {
            sessionFactory.getCurrentSession().update(authProvider);
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error updating AuthProvider");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error updating AuthProvider");
        }

        log.info("Entering :" + methodName);
    }

    @Override
    public AuthProvider getAuthProvider(String providerId, int providerType) throws BaseException {

        final String methodName = "getAuthProvider()";
        log.info("Entering :" + methodName);

        List<AuthProvider> result = null;
        try {
            result = (List<AuthProvider>) this.sessionFactory.getCurrentSession()
                    .createQuery("from AuthProvider ap where ap.providerId = :providerId and ap.providerType =:providerType")
                    .setParameter("providerId", providerId)
                    .setParameter("providerType", providerType)
                    .list();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching AuthProvider");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage());
            throw new BaseException("Error fetching AuthProvider");
        }
        log.info("Exiting :" + methodName);
        if (null == result || result.size() <= 0) {
            return null;
        }
        return result.get(0);

    }

    @Override
    public AuthProvider fetchAuthProvider(long authID) throws BaseException {
        final String methodName = "fetchAuthProvider()";
        log.info("Entering :" + methodName);
        AuthProvider authProvider = null;
        try {
            authProvider = (AuthProvider) sessionFactory.getCurrentSession().load(AuthProvider.class, authID);
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error fetching AuthProvider");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error fetching AuthProvider");
        }

        log.info("Entering :" + methodName);
        return authProvider;
    }

    @Override
    public List getListOfActiveAuthIds(List userIds, int providerType) throws BaseException {
        final String methodName = "getListOfActiveAuthIds()";
        log.info("Entering :" + methodName);

        List<Long> result = null;
        try {
            result = (List<Long>) this.sessionFactory.getCurrentSession()
                    .createQuery("select id from AuthProvider ap where ap.providerType = :providerType and ap.providerId IN :userIds and ap.status =:status")
                    .setParameter("providerType", providerType)
                    .setParameter("status", ApplicationConstants.STATUS_AUTH_USER_ACTIVE)
                    .setParameterList("userIds", userIds)
                    .list();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error fethcing list of authIds");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error fethcing list of authIds");
        }
        if (null == result || result.size() <= 0) {
            return null;
        }
        log.info("Exiting :" + methodName);
        return result;
    }

    @Override
    public void saveUpdateLocation(Location location) throws BaseException {
        final String methodName = "updateLocation()";
        log.info("Entering :" + methodName);

        try {
            sessionFactory.getCurrentSession().saveOrUpdate(location);
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error updating Location");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error updating Location");
        }

        log.info("exiting :" + methodName);

    }

    @Override
    public long saveMessage(Message message) throws BaseException {

        final String methodName = "saveMessage()";
        log.info("Entering :" + methodName);
        long id = 0;
        try {
            id = (Long) sessionFactory.getCurrentSession().save(message);
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error saving message");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error saving message");
        }

        log.info("exiting :" + methodName);
        return id;
    }

    @Override
    public List getNewMessage(long tripID, long msgID, long chatAuthID) throws BaseException {

        final String methodName = "getNewMessage()";
        log.info("Entering :" + methodName);

        List<Message> result = null;
        try {
            result = (List<Message>) this.sessionFactory.getCurrentSession()
                    .createQuery("from Message msg where msg.fromUser.id =:chatAuthID and msg.trip.tripID = :tripID and msg.id > :msgID")
                    .setParameter("tripID", tripID)
                    .setParameter("msgID", msgID)
                    .setParameter("chatAuthID", chatAuthID)
                    .list();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching message");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage());
            throw new BaseException("Error fetching message");
        }
        log.info("Exiting :" + methodName);
        return result;
    }

    @Override
    public long saveToken(Token token) throws BaseException {

        final String methodName = "saveToken()";
        log.info("Entering :" + methodName);
        long id = 0;
        try {
            id = (Long) sessionFactory.getCurrentSession().save(token);
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error saving token");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error saving token");
        }

        log.info("Exiting :" + methodName);
        return id;
    }

    @Override
    public Token fetchToken(long id) throws BaseException {
        final String methodName = "fetchToken()";
        log.info("Entering :" + methodName);
        Token token = null;
        try {
            token = (Token) sessionFactory.getCurrentSession().load(Token.class, id);
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error fetching token");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error fetching token");
        }

        log.info("Exiting :" + methodName);
        return token;
    }

    @Override
    public void updateToken(Token token) throws BaseException {

        final String methodName = "updateToken()";
        log.info("Entering :" + methodName);

        try {
            sessionFactory.getCurrentSession().update(token);

        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error updating token");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error updating token");
        }

    }

    @Override
    public List fetchUsersByDistance(double lat, double lng, Collection ids, double distance) throws BaseException {
        final String methodName = "fetchUsersByDistance()";
        log.info("Entering :" + methodName + " : AuthIDs size :" + ids.size());

        List<Long> result = null;
        try {

//    SELECT * FROM lapool.location
//     where 
//        ( 6371 * 
//                  acos( 
//        cos( radians(12.9087748) ) * 
//        COSRADIANSLAT * 
//        cos( RADIANSLNG - 
//        radians(77.6400301) ) + 
//        sin( radians(12.9087748) ) * 
//        SINRADIANSLAT 
//    ) ) < 5
            result = (List<Long>) this.sessionFactory.getCurrentSession()
                    .createQuery("select l.id from Location l where"
                            + "( 6371 * "
                            + "    acos( "
                            + "        cos( radians(:latitude) ) * "
                            + "        cosRadiansLat * "
                            + "        cos( radiansLng - "
                            + "        radians(:longitude) ) + "
                            + "        sin(radians(:latitude) ) * "
                            + "        sinRadiansLat"
                            + "    ) ) < :distance and l.id IN :ids")
                    .setParameter("latitude", lat)
                    .setParameter("longitude", lng)
                    .setParameter("distance", distance)
                    .setParameterList("ids", ids)
                    .list();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching User Locations");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage());
            throw new BaseException("Error fetching User Locations");
        }
        log.info("Exiting :" + methodName + " : AuthIDs length " + result.size());
        return result;
    }

    @Override
    public List getMessageHistory(long tripID, long msgID) throws BaseException {
        final String methodName = "getMessageHistory()";
        log.info("Entering :" + methodName + " TripID:" + tripID + " and msgID:" + msgID);

        List<Message> result = null;
        try {
            result = (List<Message>) this.sessionFactory.getCurrentSession()
                    .createQuery("from Message msg where  msg.trip.tripID = :tripID and msg.id > :msgID")
                    .setParameter("tripID", tripID)
                    .setParameter("msgID", msgID)
                    .list();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching message history");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage());
            throw new BaseException("Error fetching message history");
        }
        log.info("Exiting :" + methodName);
        return result;
    }

    @Override
    public SystemUser fetchSystemUser(long accountID, String userName) throws BaseException {
        final String methodName = "fetchSystemUser()";
        log.info("Entering :" + methodName + " : accountID:" + accountID + " and userName:" + userName);

        List<SystemUser> result = null;
        try {
            result = (List<SystemUser>) this.sessionFactory.getCurrentSession()
                    .createQuery("from SystemUser su where su.username =:username and su.account.accountID = :accountID")
                    .setParameter("accountID", accountID)
                    .setParameter("username", userName)
                    .list();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching systemUser");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage());
            throw new BaseException("Error fetching systemUser");
        }
        log.info("Exiting :" + methodName);
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    @Override
    public long fetchSystemUserCount(Map<String, Object> filters) throws BaseException {
        final String methodName = "fetchSystemUserCount()";
        log.info("Entering :" + methodName);

        long count = 0;
        Criteria criteria;

        try {
            criteria = this.sessionFactory.getCurrentSession().createCriteria(SystemUser.class);

            //Add filters
            for (Map.Entry<String, Object> filter : filters.entrySet()) {
                Criterion likeCricCriterion = Restrictions.like(filter.getKey(), filter.getValue().toString(), MatchMode.ANYWHERE);
                criteria.add(likeCricCriterion);
            }
            criteria.setProjection(Projections.rowCount());
            count = (Long) criteria.uniqueResult();

        } catch (HibernateException he) {
            log.error(methodName + ": Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching System users count");
        } catch (Exception ex) {
            log.error(methodName + " :Exception : " + ex.getMessage());
            throw new BaseException("Error fetching system users count");
        }

        return count;
    }

    @Override
    public List fetchSystemUsers(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) throws BaseException {
        final String methodName = "fetchSystemUsers()";
        log.info("Entering :" + methodName);

        List<SystemUser> results = null;
        Criteria criteria = null;

        try {
            criteria = this.sessionFactory.getCurrentSession().createCriteria(SystemUser.class);

            //Add filters
            for (Map.Entry<String, Object> filter : filters.entrySet()) {
                Criterion likeCricCriterion = Restrictions.like(filter.getKey(), filter.getValue().toString(), MatchMode.ANYWHERE);
                criteria.add(likeCricCriterion);
            }

            //Add sort order
            if (sortField != null) {
                if (sortOrder == SortOrder.ASCENDING) {
                    criteria.addOrder(Order.asc(sortField));
                } else if (sortOrder == SortOrder.DESCENDING) {
                    criteria.addOrder(Order.desc(sortField));
                }
            }
            results = criteria.setFirstResult(first).setMaxResults(pageSize).list();

        } catch (HibernateException he) {
            log.error(methodName + ": Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching system users");
        } catch (Exception ex) {
            log.error(methodName + " :Exception : " + ex.getMessage());
            throw new BaseException("Error fetching system users");
        }

        return results;
    }
    
    // ---------------------------------------- No more method here -----------------------------------

}
