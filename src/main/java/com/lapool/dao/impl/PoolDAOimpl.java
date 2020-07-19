/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.dao.impl;

import com.lapool.dao.PoolDAO;
import com.lapool.data.LocationPoint;
import com.lapool.exception.BaseException;
import com.lapool.model.Alert;
import com.lapool.model.AlertLocation;
import com.lapool.model.AuthProvider;
import com.lapool.model.PickUpRequest;
import com.lapool.model.Trip;
import com.lapool.model.TripLocation;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author akshat
 */
@Repository
public class PoolDAOimpl implements PoolDAO {

    final static Logger log = LoggerFactory.getLogger(PoolDAOimpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public long registerPickupRequest(PickUpRequest pickReq) throws BaseException {
        long result;
        try {
            result = (Long) this.sessionFactory.getCurrentSession().save(pickReq);
        } catch (HibernateException he) {
            log.error("Hibernate Exception in saving pick request: " + he.getMessage());
            throw new BaseException(he.getMessage());
        } catch (Exception e) {
            log.error("Error saving pick request: " + e.getMessage());
            throw new BaseException(e.getMessage());
        }
        return result;
    }

    @Override
    public List getPickupRequests(Set<Long> authIds, Timestamp fromCreatedTime, int status, LocationPoint p1, LocationPoint p2, LocationPoint p3, LocationPoint p4, int pageNumber, int pageSize, boolean listFemaleReqs) throws BaseException {
        //TODO: All model objects to be reviewed. Parent - child fetch is recurrsive
        // The fetch below returns additional rows as well
        final String methodName = "getPickupRequests()";
        log.info("Entering :" + methodName);

        List result = null;
        try {
            Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(PickUpRequest.class);
            criteria.add(Restrictions.gt("created", fromCreatedTime));
            criteria.add(Restrictions.eq("status", status));
            criteria.createCriteria("authProvider").add(Restrictions.in("id", authIds));

            criteria.add(Restrictions.gt("pickUpLat", p3.getLat()));
            criteria.add(Restrictions.lt("pickUpLat", p1.getLat()));
            criteria.add(Restrictions.lt("pickUpLng", p2.getLng()));
            criteria.add(Restrictions.gt("pickUpLng", p4.getLng()));
            if (!listFemaleReqs) {
                criteria.add(Restrictions.eq("girlsOnly", 0));
            }

            criteria.setMaxResults(pageSize);
            criteria.setFirstResult((pageNumber - 1) * pageSize);

            result = criteria.list();

        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error fethcing list of pickupRequests");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error fethcing list of pickupRequests");
        }
        if (null == result || result.size() <= 0) {
            return null;
        }
        log.info("Exiting :" + methodName);
        return result;
    }

    @Override
    public long saveTrip(Trip trip) throws BaseException {

        final String methodName = "saveTrip()";
        log.info("Entering :" + methodName);
        Long id;
        try {
            id = (Long) sessionFactory.getCurrentSession().save(trip);

        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error saving Trip details");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error saving Trip details");
        }

        return id;
    }

    @Override
    public PickUpRequest getPickUpRequest(long pickID) throws BaseException {

        final String methodName = "getPickUpRequest() for pickID: " + pickID;
        log.info("Entering :" + methodName);
        PickUpRequest pick = null;
        try {
            pick = (PickUpRequest) sessionFactory.getCurrentSession().get(PickUpRequest.class, pickID);

        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error fetching pickupRequests");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error fetching pickupRequests");
        }
        return pick;
    }

    @Override
    public Trip getTripDetails(long tripID) throws BaseException {
        final String methodName = "getTripDetails() for tripID: " + tripID;
        log.info("Entering :" + methodName);

        Trip trip = null;
        try {
            trip = (Trip) sessionFactory.getCurrentSession().get(Trip.class, tripID);

        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error fetching Trip details");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error fetching Trip details");
        }
        return trip;
    }

    @Override
    public long saveUpdatePickupRequest(PickUpRequest pickupReq) throws BaseException {
        final String methodName = "savePickupRequest()";
        log.info("Entering :" + methodName);

        try {
            sessionFactory.getCurrentSession().saveOrUpdate(pickupReq);

        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error saving pickupRequest");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error saving pickupRequests");
        }

        return pickupReq.getPickID();
    }

    @Override
    public long updateUserTripLocation(TripLocation location, long authID, long tripID) throws BaseException {
        final String methodName = "updateUserTripLocation()";
        log.info("Entering :" + methodName);

        try {
            Session session = sessionFactory.getCurrentSession();

            location.setAuthProvider((AuthProvider) session.load(AuthProvider.class, authID));
            location.setTrip((Trip) session.load(Trip.class, tripID));

            session.save(location);

        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error saving TripLocation");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error saving TripLocation");
        }

        return location.getId();
    }

    @Override
    public boolean isTripKeyExist(String tripKey) throws BaseException {
        final String methodName = "isTripKeyExist()";
        log.info("Entering :" + methodName);

        Long resultLength = null;
        try {
            resultLength = (Long) this.sessionFactory.getCurrentSession()
                    .createQuery("select count(*) from Trip t where t.tripKey = :key")
                    .setParameter("key", tripKey)
                    .setMaxResults(1)
                    .uniqueResult();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error checking Trip key");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage(), ex);
            throw new BaseException("Error checking trip key");
        }
        log.info("Exiting :" + methodName);
        return resultLength > 0;
    }

    @Override
    public Trip fetchTripDetails(String key) throws BaseException {

        final String methodName = "fetchTripDetails()";
        log.info("Entering :" + methodName);
        Trip trip = null;

        try {
            Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Trip.class);
            trip = (Trip) criteria.add(Restrictions.eq("tripKey", key))
                    .uniqueResult();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error checking Trip key");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage(), ex);
            throw new BaseException("Error checking trip key");
        }

        log.info("Exiting :" + methodName);
        return trip;
    }

    @Override
    public List<TripLocation> fetchTripLocations(long tripID) throws BaseException {
        final String methodName = "fetchTripLocations()";
        log.info("Entering :" + methodName + " - tripID : " + tripID);
        List locations = null;

        try {
            locations = (List) this.sessionFactory.getCurrentSession()
                    .createQuery("from TripLocation tl where tl.trip.tripID = :tripID")
                    .setParameter("tripID", tripID)
                    .list();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error checking Trip key");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage(), ex);
            throw new BaseException("Error checking trip key");
        }

        log.info("Exiting :" + methodName);
        return locations;
    }

    @Override
    public long saveAlertDetails(Alert alert, long tripID, long authID) throws BaseException {
        final String methodName = "saveAlertDetails()";
        log.info("Entering :" + methodName);

        try {
            Session session = sessionFactory.getCurrentSession();

            alert.setAuthProvider((AuthProvider) session.load(AuthProvider.class, authID));
            if (tripID > 0) {
                alert.setTrip((Trip) session.load(Trip.class, tripID));
            } else {
                alert.setTrip(null);
            }

            session.save(alert);

        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error saving Alert details");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error saving Alert details");
        }
        return alert.getId();
    }

    @Override
    public long saveAlertLocation(AlertLocation alertLocation, long alertID) throws BaseException {
        final String methodName = "saveAlertLocation()";
        log.info("Entering :" + methodName + " - alertID:" + alertID);

        try {
            Session session = sessionFactory.getCurrentSession();

            alertLocation.setAlert((Alert) session.load(Alert.class, alertID));
            session.save(alertLocation);

        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error saving alert location");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error saving alert location");
        }

        return alertLocation.getId();
    }

    @Override
    public List fetchPublicPickRequests(long authID, Timestamp fromCreatedTime, int status, LocationPoint p1, LocationPoint p2, LocationPoint p3, LocationPoint p4, int pageNumber, int pageSize, boolean listFemalePickReqs) throws BaseException {
        final String methodName = "fetchPublicPickRequests()";
        log.info("Entering :" + methodName);

        List result = null;
        try {
            Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(PickUpRequest.class);
            criteria.add(Restrictions.gt("created", fromCreatedTime));
            criteria.add(Restrictions.eq("status", status));
            criteria.createCriteria("authProvider").add(Restrictions.ne("id", authID));
            criteria.add(Restrictions.eq("openToAll", 1));
            criteria.add(Restrictions.gt("pickUpLat", p3.getLat()));
            criteria.add(Restrictions.lt("pickUpLat", p1.getLat()));
            criteria.add(Restrictions.lt("pickUpLng", p2.getLng()));
            criteria.add(Restrictions.gt("pickUpLng", p4.getLng()));
            if (!listFemalePickReqs) {
                criteria.add(Restrictions.eq("girlsOnly", 0));
            }

            criteria.setMaxResults(pageSize);
            criteria.setFirstResult((pageNumber - 1) * pageSize);

            result = criteria.list();

        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + methodName + " :" + he.getMessage());
            throw new BaseException("Error fethcing list of pickupRequests");
        } catch (Exception ex) {
            log.error("Exception : " + methodName + " :" + ex.getMessage());
            throw new BaseException("Error fethcing list of pickupRequests");
        }
        if (null == result || result.size() <= 0) {
            return null;
        }
        log.info("Exiting :" + methodName);
        return result;
    }

    @Override
    public Alert fetchAlertDetals(String key) throws BaseException {
        final String methodName = "fetchAlertDetals()";
        log.info("Entering :" + methodName);
        Alert alert = null;

        try {
            Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Alert.class);
            alert = (Alert)criteria.add(Restrictions.eq("alertKey", key))
                    .uniqueResult();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error checking alert key");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage(), ex);
            throw new BaseException("Error checking alert key");
        }

        log.info("Exiting :" + methodName);
        return alert;    
    }

}
