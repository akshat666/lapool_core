/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.service.network;

import com.lapool.annotation.Secured;
import com.lapool.bo.NetworkBO;
import com.lapool.data.EmailVerificationBean;
import com.lapool.exception.BaseException;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author akshat666
 */
@Component
@Path("/network")
public class NetworkService {

    final static Logger log = LoggerFactory.getLogger(NetworkService.class);
    

    private NetworkBO networkBO;

    @Autowired
    public void setNetworkBO(NetworkBO networkBO) {
        this.networkBO = networkBO;
    }

    @Secured
    @POST
    @Path("/channel/verifyemail")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response verifyEmail(EmailVerificationBean emailVerificationBean, @HeaderParam("token") String token) throws BaseException {
        final String methodName = "verifyEmail()";
        log.info("Entering " + methodName);

        //Validate token
        
        networkBO.saveChannelEmail(emailVerificationBean);

        log.info("Exiting " + methodName);
        //Set code as 201 for new creation of new record
        return Response.status(201).build();

    }
    
    @Secured
    @GET
    @Path("/channel/details/{authID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchUserChannel(@PathParam("authID") long authID, @HeaderParam("token") String token) throws BaseException
    {
        final String methodName = "fetchUserChannel()";
        log.info("Entering :" + methodName);
        
        //Validate token

        List result = networkBO.fetchUserChannelDetails(authID);
        return Response.status(Response.Status.OK).entity(result).build();
    }
}
