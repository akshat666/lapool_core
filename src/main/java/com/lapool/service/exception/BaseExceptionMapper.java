/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.service.exception;

import com.lapool.data.ErrorMessage;
import com.lapool.exception.BaseException;
import com.lapool.util.ApplicationConstants;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author akshat
 */
@Provider
public class BaseExceptionMapper implements ExceptionMapper<BaseException> {

    @Override
    public Response toResponse(BaseException ex) {

        return Response.status(ex.getStatus())
                .entity(
                        new ErrorMessage(
                                ex.getMessage(),
                                ex.getCode(),
                                ex.getStatus(),
                                ex.getLink(),
                                ex.getDevMessage())
                        )
                .type(MediaType.APPLICATION_JSON).
                build();

    }

}
