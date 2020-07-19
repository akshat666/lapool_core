/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.service.user;

import com.lapool.annotation.Secured;
import com.lapool.bo.UserBO;
import com.lapool.bo.UserExtBO;
import com.lapool.data.fb.FBLoginBean;
import com.lapool.data.LocationPoint;
import com.lapool.data.MessageBean;
import com.lapool.data.MutualFriendsBean;
import com.lapool.data.UserCurrentStatus;
import com.lapool.data.UserBean;
import com.lapool.exception.BaseException;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
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
@Path("/user")
public class UserService {

    final static Logger log = LoggerFactory.getLogger(UserService.class);

    private UserBO userBO;

    @Autowired
    public void setUserBO(UserBO userBO) {
        this.userBO = userBO;
    }

    private UserExtBO userExtBO;

    @Autowired
    public void setUserExtBO(UserExtBO userExtBO) {
        this.userExtBO = userExtBO;
    }

    @POST
    @Path("/fblogin/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserBean saveFBLoginDetails(FBLoginBean fbLoginBean) throws BaseException {
        final String methodName = "saveFBLoginDetails()";
        log.info("Entering " + methodName);

        UserBean userBean = userBO.saveFBUserLoginDetails(fbLoginBean);

        log.info("Exiting " + methodName);
        //Set code as 201 for new creation of new record
        return userBean;

    }

    @Secured
    @POST
    @Path("/location/update/{authID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserLocation(@PathParam("authID") long authID,
            LocationPoint location,
            @HeaderParam("token") String token) throws BaseException {

        final String methodName = "updateUserLocation()";
        log.info("Entering " + methodName);

        userBO.updateUserLocation(authID, location);

        log.info("Exiting " + methodName);

        return Response.status(Response.Status.ACCEPTED).build();
    }

//    @POST
//    @Path("/work/verifyemail")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response verifyEmail(EmailVerificationBean emailVerificationBean, @HeaderParam("token") String token) throws BaseException {
//        final String methodName = "verifyEmail()";
//        log.info("Entering " + methodName);
//
//        networkBO.saveChannelEmail(emailVerificationBean);
//
//        log.info("Exiting " + methodName);
//        //Set code as 201 for new creation of new record
//        return Response.status(201).build();
//
//    }
    /**
     * Method takes incoming requests to check if user is present in the system
     *
     * @param userBean
     * @return
     * @throws BaseException
     */
    @POST
    @Path("/exists")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserBean checkUserPresent(UserBean userBean) throws BaseException {
        final String methodName = "isUserPresent()";
        log.info("Entering " + methodName);

        UserBean result = userBO.checkUserPresent(userBean);

        log.info("Exiting " + methodName);
        return result;

    }

    @Secured
    @POST
    @Path("/message/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public long sendMessage(MessageBean message,
            @HeaderParam("token") String token) throws BaseException {

        final String methodName = "sendMessage()";
        log.info("Entering " + methodName);

        //Validate token
        long id = userBO.saveMessage(message);

        log.info("Exiting " + methodName);
        return id;
    }

    @Secured
    @GET
    @Path("/message/get")
    @Produces(MediaType.APPLICATION_JSON)
    public List getNewMessages(@QueryParam("tripID") long tripID, @QueryParam("msgID") long msgID, @QueryParam("chatAuthID") long chatAuthID,
            @HeaderParam("token") String token) throws BaseException {

        final String methodName = "getNewMessage()";
        log.info("Entering " + methodName);

        List<MessageBean> messages = userBO.getNewMessage(tripID, msgID, chatAuthID);

        log.info("Exiting " + methodName);
        //Set code as 201 for new creation of new record
        return messages;
    }

    @POST
    @Path("/fblogin/update/{authID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateFBUserToken(@PathParam("authID") long authID, FBLoginBean fbLoginBean) throws BaseException {
        final String methodName = "updateFBUserToken()";
        log.info("Entering " + methodName);

        userExtBO.updateFBUserToken(authID, fbLoginBean);
        
        log.info("Exiting " + methodName);

    }
    
    
    @Secured
    @GET
    @Path("/current/status")
    @Produces(MediaType.APPLICATION_JSON)
    public UserCurrentStatus fetchUserCurrentState(@HeaderParam("authID") long authID) throws BaseException {
        final String methodName = "fetchUserCurrentState()";
        log.info("Entering :" + methodName);

        return userExtBO.fetchUserCurrentState(authID);
    }
    
    
    @Secured
    @POST
    @Path("/mutual/connections")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MutualFriendsBean fetchMutualConnections(Map<String, Object> json) throws BaseException {
        final String methodName = "fetchMutualConnections()";
        log.info("Entering : :" + methodName);

        Long authIDone = (Long) json.get("authID");
        Long authIDtwo = (Long) json.get("userInReqAuthID");

        return userExtBO.fetchMutualConnections(authIDone, authIDtwo);

    }

}
