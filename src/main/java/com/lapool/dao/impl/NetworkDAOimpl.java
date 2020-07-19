/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.dao.impl;

import com.lapool.dao.NetworkDAO;
import com.lapool.exception.BaseException;
import com.lapool.model.Channel;
import com.lapool.model.UserChannel;
import com.lapool.util.ApplicationConstants;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
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
public class NetworkDAOimpl implements NetworkDAO {

    final static Logger log = LoggerFactory.getLogger(UserDAOimpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Long> getActiveUsersSubscribed(Collection channelIDList) throws BaseException {
        final String methodName = "getActiveUsersSubscribed()";
        log.info("Entering :" + methodName);
        List<Long> resultAuthIds = null;

        try {
            resultAuthIds = (List<Long>) this.sessionFactory.getCurrentSession()
                    .createQuery("select uc.authProvider.id from UserChannel uc join uc.channel ch where ch.id IN :channels and uc.authProvider.status = :active and uc.status =:status")
                    .setParameterList("channels", channelIDList)
                    .setParameter("active", ApplicationConstants.STATUS_AUTH_USER_ACTIVE)
                    .setParameter("status", ApplicationConstants.STATUS_USERCHANNEL_ACTIVE)
                    .list();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching subscribed authID");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage());
            throw new BaseException("Error fetching subscribed authID");
        }

        log.info("Exiting :" + methodName);
        return resultAuthIds;
    }

    @Override
    public List getUserChannelsFromAuthID(long authID) throws BaseException {
        final String methodName = "getChannelFromAuthID()";
        log.info("Entering :" + methodName);
        List<Channel> result = null;

        try {
            result = (List) this.sessionFactory.getCurrentSession()
                    .createQuery("select cd.channel from AuthProvider ap join ap.userChannels cd where ap.id = :authID and cd.status =:status")
                    .setParameter("authID", authID)
                    .setParameter("status", ApplicationConstants.STATUS_USERCHANNEL_ACTIVE)
                    .list();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching subscribed authID");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage());
            throw new BaseException("Error fetching subscribed authID");
        }

        log.info("Exiting :" + methodName);
        if (null == result || result.size() <= 0) {
            return null;
        }
        return result;
    }

    @Override
    public Channel getChannel(long channelID) throws BaseException {
        final String methodName = "getChannel()";
        log.info("Entering :" + methodName);

        Channel result = null;
        try {
            result = (Channel) this.sessionFactory.getCurrentSession().load(Channel.class, channelID);

        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error checking Channels in the system");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage(), ex);
            throw new BaseException("Error checking Channels in the System");
        }

        log.info("Exiting :" + methodName);
        return result;
    }
//        @Override
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

    @Override
    public void saveChannelDetails(UserChannel userChannel) throws BaseException {
        final String methodName = "saveChannelDetails()";
        log.info("Entering :" + methodName);

        try {
            this.sessionFactory.getCurrentSession().save(userChannel);
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error saving Work Details");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage());
            throw new BaseException("Error saving Work Details");
        }
        log.info("Exiting :" + methodName);
    }

    @Override
    public List fetchUserChannels(long authID) throws BaseException {

        final String methodName = "fetchUserChannels()";
        log.info("Entering :" + methodName);
        List<UserChannel> result = null;

        try {
            result = (List<UserChannel>) this.sessionFactory.getCurrentSession()
                    .createQuery("from UserChannel uc where uc.authProvider.id = :authID order by created desc")
                    .setParameter("authID", authID)
                    .list();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching subscribed authID");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage());
            throw new BaseException("Error fetching subscribed authID");
        }

        log.info("Exiting :" + methodName);
        return result;
    }

    @Override
    public void saveChannel(Channel channel) throws BaseException {
        final String methodName = "saveChannel()";
        log.info("Entering :" + methodName);

        try {
            this.sessionFactory.getCurrentSession().save(channel);
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error saving channel -" + he.getMessage());
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage());
            throw new BaseException("Error saving channel -" + ex.getMessage());
        }

        log.info("Exiting :" + methodName);

    }

    @Override
    public void updateChannel(Channel channel) throws BaseException {
        final String methodName = "updateChannel()";
        log.info("Entering :" + methodName);
        try {
            this.sessionFactory.getCurrentSession().update(channel);
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error saving channel");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage());
            throw new BaseException("Error saving channel");
        }

        log.info("Exiting :" + methodName);
    }

    @Override
    public List fetchChannels(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) throws BaseException {
        final String methodName = "fetchChannels()";
        log.info("Entering :" + methodName);

        List<Channel> results = null;
        Criteria criteria = null;

        try {
            criteria = this.sessionFactory.getCurrentSession().createCriteria(Channel.class);

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
            throw new BaseException("Error fetching channels");
        } catch (Exception ex) {
            log.error(methodName + " :Exception : " + ex.getMessage());
            throw new BaseException("Error fetching channels");
        }

        return results;
    }

    @Override
    public long fetchChannelCount(Map<String, Object> filters) throws BaseException {
        final String methodName = "fetchChannelCount()";
        log.info("Entering :" + methodName);

        long count = 0;
        Criteria criteria;

        try {
            criteria = this.sessionFactory.getCurrentSession().createCriteria(Channel.class);

            //Add filters
            for (Map.Entry<String, Object> filter : filters.entrySet()) {
                Criterion likeCricCriterion = Restrictions.like(filter.getKey(), filter.getValue().toString(), MatchMode.ANYWHERE);
                criteria.add(likeCricCriterion);
            }
            criteria.setProjection(Projections.rowCount());
            count = (Long) criteria.uniqueResult();

        } catch (HibernateException he) {
            log.error(methodName + ": Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching channels");
        } catch (Exception ex) {
            log.error(methodName + " :Exception : " + ex.getMessage());
            throw new BaseException("Error fetching channels");
        }

        return count;
    }

    @Override
    public List fetchChannelsByQuery(String field, String query) throws BaseException {
        final String methodName = "fetchChannelsByQuery()";
        log.info("Entering :" + methodName);

        List<Channel> results = null;
        Criteria criteria = null;

        try {
            criteria = this.sessionFactory.getCurrentSession().createCriteria(Channel.class);

            //Add filters
            Criterion likeCricCriterion = Restrictions.like(field, query, MatchMode.ANYWHERE);
            criteria.add(likeCricCriterion);

            results = criteria.list();

        } catch (HibernateException he) {
            log.error(methodName + ": Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching channels");
        } catch (Exception ex) {
            log.error(methodName + " :Exception : " + ex.getMessage());
            throw new BaseException("Error fetching channels");
        }

        return results;
    }
}
