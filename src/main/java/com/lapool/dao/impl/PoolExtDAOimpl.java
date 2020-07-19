/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.dao.impl;

import com.lapool.dao.PoolExtDAO;
import com.lapool.exception.BaseException;
import com.lapool.model.Path;
import com.lapool.model.PickUpRequest;
import com.lapool.model.Trip;
import com.lapool.util.ApplicationConstants;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author akshat666
 */
public class PoolExtDAOimpl implements PoolExtDAO {

    final static Logger log = LoggerFactory.getLogger(PoolExtDAOimpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Trip fetchLastTripAsDropper(long authID) throws BaseException {
        final String methodName = "fetchLastTripAsDropper()";
        log.info("Entering :" + methodName + " - authID : " + authID);
        Trip trip = null;

        try {
            trip = (Trip) this.sessionFactory.getCurrentSession()
                    .createQuery("from Trip t where t.authProvider.id = :authID ORDER BY id DESC")
                    .setParameter("authID", authID)
                    .setMaxResults(1)
                    .uniqueResult();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching recent Trip");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage(), ex);
            throw new BaseException("Error fetching recent Trip");
        }

        log.info("Exiting :" + methodName);
        return trip;
    }

    @Override
    public PickUpRequest fetchLastPickupRequest(long authID) throws BaseException {
        final String methodName = "fetchLastPickupRequest()";
        log.info("Entering :" + methodName + " - authID : " + authID);
        PickUpRequest pickUpRequest = null;

        try {
            pickUpRequest = (PickUpRequest) this.sessionFactory.getCurrentSession()
                    .createQuery("from PickUpRequest pr where pr.authProvider.id = :authID ORDER BY id DESC")
                    .setParameter("authID", authID)
                    .setMaxResults(1)
                    .uniqueResult();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching recent PickupRequest");
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage(), ex);
            throw new BaseException("Error fetching recent PickupRequest");
        }

        log.info("Exiting :" + methodName);
        return pickUpRequest;
    }

    @Override
    public long totalPathsForAuthID(long authID) throws BaseException {
        final String methodName = "totalPathsForAuthID()";
        log.info("Entering :" + methodName + " - authID : " + authID);
        long totalPaths = 0;

        try {
            totalPaths = (Long) this.sessionFactory.getCurrentSession()
                    .createQuery("select count(*) from Path p where p.authProvider.id = :authID AND p.status = 1")
                    .setParameter("authID", authID)
                    .uniqueResult();
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error in counting total Paths for authID:" + authID);
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage(), ex);
            throw new BaseException("Error in counting total Paths for authID:" + authID);
        }

        log.info("Exiting :" + methodName);
        return totalPaths;
    }

    @Override
    public long savePath(Path path) throws BaseException {
        final String methodName = "totalPathsForAuthID()";
        log.info("Entering "+methodName+" - authID:"+path.getAuthProvider().getId());
        long result;
        try {
            result = (Long) this.sessionFactory.getCurrentSession().save(path);
        } catch (HibernateException he) {
            log.error("Hibernate Exception in saving Path: " + he.getMessage());
            throw new BaseException(he.getMessage());
        } catch (Exception e) {
            log.error("Error saving Path: " + e.getMessage());
            throw new BaseException(e.getMessage());
        }
        log.info("Exiting :" + methodName);
        return result;
    }

    @Override
    public List fetchUsersOnPath(double pickUpLat, double pickUpLng, double dropLat, double dropLng, double distance, int day_of_week) throws BaseException {
        final String methodName = "fetchUsersOnPath()";
        log.info("Entering :" + methodName + " - pickUpLat : " + pickUpLat);
        List<Long> authIDs = null;

        String subQueryDayOfWeek = null;

        Date d1 = new Date();
        Date range1 = new Date(d1.getTime() - ApplicationConstants.PICKUPREQ_EXPIRE_TIME_INTERVAL);
        Date range2 = new Date(d1.getTime() + ApplicationConstants.PICKUPREQ_EXPIRE_TIME_INTERVAL);

        switch (day_of_week) {
            case 1:
                subQueryDayOfWeek = "and p.sun = 1";
                break;
            case 2:
                subQueryDayOfWeek = "and p.mon = 1";
                break;
            case 3:
                subQueryDayOfWeek = "and p.tue = 1";
                break;
            case 4:
                subQueryDayOfWeek = "and p.wed = 1";
                break;
            case 5:
                subQueryDayOfWeek = "and p.thu = 1";
                break;
            case 6:
                subQueryDayOfWeek = "and p.fri = 1";
                break;
            case 7:
                subQueryDayOfWeek = "and p.sat = 1";
                break;
        }

        try {
            authIDs = (List<Long>) this.sessionFactory.getCurrentSession()
                    .createQuery("select p.authProvider.id from Path p where p.status = 1 and "
                            + "( 6371 * "
                            + "    acos( "
                            + "        cos( radians(:pickUpLat) ) * "
                            + "        startCosRadiansLat * "
                            + "        cos( startRadiansLng - "
                            + "        radians(:pickUpLng) ) + "
                            + "        sin(radians(:pickUpLat) ) * "
                            + "        startSinRadiansLat"
                            + "    ) ) < :distance and "
                            + "( 6371 * "
                            + "    acos( "
                            + "        cos( radians(:dropLat) ) * "
                            + "        destCosRadiansLat * "
                            + "        cos( destRadiansLng - "
                            + "        radians(:dropLng) ) + "
                            + "        sin(radians(:dropLat) ) * "
                            + "        destSinRadiansLat"
                            + "    ) ) < :distance "
                            + subQueryDayOfWeek
                            + " and p.startTime > :range1 and startTime < :range2"
                    )
                    .setParameter("pickUpLat", pickUpLat)
                    .setParameter("pickUpLng", pickUpLng)
                    .setParameter("dropLat", dropLat)
                    .setParameter("dropLng", dropLng)
                    .setParameter("distance", distance)
                    .setParameter("range1", range1)
                    .setParameter("range2", range2)
                    .list();

        } catch (HibernateException he) {
            log.error("Hibernate Exception in matching Paths with PickReQs : " + he.getMessage());
            throw new BaseException("Error in matching Paths with PickReQs ");
        } catch (Exception ex) {
            log.error("Exception in matching Paths with PickReQs : " + ex.getMessage(), ex);
            throw new BaseException("Error in matching Paths with PickReQs ");
        }

        log.info("Exiting :" + methodName);
        return authIDs;
    }

    @Override
    public List fetchActivePaths(long authID) throws BaseException {
        final String methodName = "fetchActivePaths()";
        log.info("Entering :" + methodName + " - authID : " + authID);
        List paths = null;

        try {
            paths = (List) this.sessionFactory.getCurrentSession()
                    .createQuery("from Path p where p.authProvider.id = :authID and p.status = 1")
                    .setParameter("authID", authID)
                    .list();
        } catch (HibernateException he) {
            log.error("Hibernate Exception in fetching PATHs: " + he.getMessage());
            throw new BaseException("Error in fetching PATHs");
        } catch (Exception ex) {
            log.error("Exception in fetching PATHs : " + ex.getMessage(), ex);
            throw new BaseException("Error in fetching PATHs");
        }

        log.info("Exiting :" + methodName);
        return paths;    }

    @Override
    public Path fetchUserPath(long pathID) throws BaseException {
        final String methodName = "deleteUserPath()";
        log.info("Entering :" + methodName + " - pathID : " + pathID);
        
        Path path = null;
        
        try{
           path = (Path)sessionFactory.getCurrentSession().load(Path.class, pathID);
        }catch(HibernateException he){
            log.error("Exception in fetching user PATH : " + he.getMessage(), he);
            throw new BaseException("Error in fetching PATH");
        }catch(Exception ex){
            log.error("Exception in fetching user PATH : " + ex.getMessage(), ex);
            throw new BaseException("Error in fetching PATH");
        }
        log.info("Exiting :" + methodName);
        return path;
        
        
    }

}
