/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.filter;

import com.lapool.annotation.Secured;
import com.lapool.bo.UserBO;
import com.lapool.exception.BaseException;
import com.lapool.util.ApplicationConstants;
import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author akshat666
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    final static Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    private UserBO userBO;

    @Autowired
    public void setUserBO(UserBO userBO) {
        this.userBO = userBO;
    }

    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        final String methodName = "filter()";
        log.info("Entering :" + methodName);

        String token = crc.getHeaderString(ApplicationConstants.TOKEN);
        Long authID = Long.parseLong(crc.getHeaderString(ApplicationConstants.AUTHID));
        if (token == null || authID == null || token.isEmpty()) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        try {
            // Validate the token
            validateToken(token, authID);

        } catch (Exception e) {
            crc.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
        }
        log.info("Exiting :" + methodName);

    }

    @Transactional
    private void validateToken(String token, long authID) throws BaseException {
        final String methodName = "validateToken()";
        log.info("Entering and exiting :" + methodName+" for authID:"+authID+" and token:"+token);

        if (!userBO.isTokenValid(authID, token)) {
            log.info("Invalid token :"+methodName);
            throw new BaseException(ApplicationConstants.MSG_UNAUTHORIZED);
        }

    }

}
