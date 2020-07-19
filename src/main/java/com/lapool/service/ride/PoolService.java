/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.service.ride;

import com.lapool.annotation.Secured;
import com.lapool.bo.PoolBO;
import com.lapool.bo.PoolExtBO;
import com.lapool.data.AlertDataBean;
import com.lapool.data.PickUpRequestBean;
import com.lapool.data.ChangeStatusBean;
import com.lapool.data.HopResult;
import com.lapool.data.LocationPoint;
import com.lapool.data.PathBean;
import com.lapool.data.TripBean;
import com.lapool.exception.BaseException;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author akshat
 */
@Component
@Path("/pool")
public class PoolService {

    final static Logger log = LoggerFactory.getLogger(PoolService.class);

    private PoolBO poolBO;

    @Autowired
    public void setPoolBO(PoolBO poolBO) {
        this.poolBO = poolBO;
    }

    private PoolExtBO poolExtBO;

    @Autowired
    public void setPoolExtBO(PoolExtBO poolExtBO) {
        this.poolExtBO = poolExtBO;
    }

    @Secured
    @POST
    @Path("/pickuprequest")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public HopResult createPickUpRequest(PickUpRequestBean pickRequest, @HeaderParam(value = "token")
            String token) throws BaseException {
        final String methodName = "createPickUpRequest()";
        log.info("Entering :" + methodName);
        
        HopResult result = poolBO.registerPickUpRequest(pickRequest);

        log.info("Exiting :" + methodName);
        return result;
    }

    @Secured
    @GET
    @Path("/pickuprequest/list/{authID}/{isReqPublicList}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PickUpRequestBean> getPickRequestList(
            @PathParam("authID") long authID,
            @PathParam("isReqPublicList") boolean isPublic,
            @QueryParam("lat") double lat,
            @QueryParam("lng") double lng,
            @QueryParam("pageNumber") int pageNumber,
            @QueryParam("pageSize") int pageSize,
            @HeaderParam("token") String token) throws BaseException {
        final String methodName = "getPickRequestList()";
        log.info("Entering : :" + methodName);

        List pickReqs = null;

        if (isPublic) {
            pickReqs = poolBO.fetchPublicPickRequestsForAuthID(authID, lat, lng, pageNumber, pageSize);
        } else {
            pickReqs = poolBO.getPickRequestsForAuthID(authID, lat, lng, pageNumber, pageSize);
        }

        log.info("Exiting :" + methodName);
        return pickReqs;
    }

    @Secured
    @POST
    @Path("/pickuprequest/confirm")
    @Consumes(MediaType.APPLICATION_JSON)
    public long confirmPickRequest(Map<String, Object> json,
            @HeaderParam("token") String token) throws BaseException {
        final String methodName = "confirmPickRequest()";
        log.info("Entering : :" + methodName);

        //Validate token
        Long authID = (Long) json.get("userAuthID");
        Long pickupID = (Long) json.get("pickID");
        Double dropperLat = (Double) json.get("dropperLat");
        Double dropperLng = (Double) json.get("dropperLng");

        long tripID = poolBO.confirmPickupRequest(authID, pickupID, dropperLat, dropperLng);
        return tripID;
    }

    @Secured
    @GET
    @Path("/trip/fetch/{tripID}")
    @Produces(MediaType.APPLICATION_JSON)
    public TripBean getTripDetails(@PathParam("tripID") long tripID, @HeaderParam("token") String token) throws BaseException {
        final String methodName = "getTripDetils()";
        log.info("Entering :" + methodName);

        return poolBO.getTripDetails(tripID);
    }

    @Secured
    @POST
    @Path("/pickuprequest/cancel")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cancelPickupRequest(ChangeStatusBean cancelBean, @HeaderParam("token") String token) throws BaseException {
        final String methodName = "cancelPickupRequest()";
        log.info("Entering :" + methodName);

        //Validate token
        long pickupID = cancelBean.getId();
        int cancelType = cancelBean.getStatus();

        poolBO.changePickupRequestStatus(pickupID, cancelType);

        log.info("Exiting :" + methodName);
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @Secured
    @POST
    @Path("trip/cancel")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response endTrip(ChangeStatusBean tripCancelBean, @HeaderParam("token") String token) throws BaseException {

        final String methodName = "cancelTrip()";
        log.info("Entering :" + methodName);

        long tripID = poolBO.endTrip(tripCancelBean);

        return Response.status(Response.Status.ACCEPTED).build();

    }

    @Secured
    @POST
    @Path("/trip/location/update/{tripID}/{authID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserTripLocation(@PathParam("tripID") long tripID, @PathParam("authID") long authID,
            LocationPoint location) throws BaseException {

        final String methodName = "updateUserLocation()";
        log.info("Entering " + methodName);

        poolBO.updateUserTripLocation(tripID, authID, location);

        log.info("Exiting " + methodName);

        return Response.status(Response.Status.ACCEPTED).build();
    }

    @Secured
    @POST
    @Path("/trip/alert/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AlertDataBean saveAlert(AlertDataBean alertDataBean) throws BaseException {

        final String methodName = "saveAlert()";
        log.info("Entering and exiting" + methodName);

        return poolBO.saveAlertDetails(alertDataBean);

    }

    @Secured
    @POST
    @Path("/alert/location/update/{alertID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAlertLocation(@PathParam("alertID") long alertID,
            LocationPoint location) throws BaseException {

        final String methodName = "updateUserLocation()";
        log.info("Entering " + methodName);

        poolBO.updateAlertLocation(alertID, location);

        log.info("Exiting " + methodName);

        return Response.status(Response.Status.ACCEPTED).build();
    }

    @Secured
    @GET
    @Path("/pickuprequest/fetch/{pickReqID}")
    @Produces(MediaType.APPLICATION_JSON)
    public PickUpRequestBean fetchPickupRequest(@PathParam(value = "pickReqID") long pickReqID) throws BaseException {
        final String methodName = "fetchPickupRequestStatus()";
        log.info("Entering :" + methodName);

        return poolExtBO.fetchPickRequestIfActive(pickReqID);
    }

    @Secured
    @POST
    @Path("/path/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveUserPath(@HeaderParam("authID") long authID, PathBean path) throws BaseException {

        final String methodName = "saveUserPath()";
        log.info("Entering " + methodName);

        poolExtBO.saveUserPath(authID, path);

        log.info("Exiting " + methodName);

        return Response.status(Response.Status.ACCEPTED).build();
    }
    
    @Secured
    @GET
    @Path("/path/list/fetch")
    @Consumes(MediaType.APPLICATION_JSON)
    public List fetchUserPaths(@HeaderParam("authID") long authID) throws BaseException {

        final String methodName = "fetchUserPaths()";
        log.info("Entering and exiting" + methodName);

        return poolExtBO.fetchActivePathList(authID);

    }
    
    @Secured
    @PUT
    @Path("/path/delete/{pathID}")
    public void deleteUserPath(@PathParam(value = "pathID") long pathID) throws BaseException{
        
        final String methodName = "deleteUserPath()";
        log.info("Entering and exiting" + methodName);
        
        poolExtBO.deleteUserPath(pathID);
        
    }
}
