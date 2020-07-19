/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.client;

import com.lapool.exception.BaseException;
import com.lapool.util.ApplicationConstants;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author akshat666
 */
public class OneSignalRESTClient {
    
    final static Logger log = LoggerFactory.getLogger(OneSignalRESTClient.class);
    
    private String oneSignalAppID;
    private String oneSignalRestApiKey;
    private String onesignalNotificationURL;

    public String getOneSignalAppID() {
        return oneSignalAppID;
    }

    public void setOneSignalAppID(String oneSignalAppID) {
        this.oneSignalAppID = oneSignalAppID;
    }

    public String getOneSignalRestApiKey() {
        return oneSignalRestApiKey;
    }

    public void setOneSignalRestApiKey(String oneSignalRestApiKey) {
        this.oneSignalRestApiKey = oneSignalRestApiKey;
    }

    public String getOnesignalNotificationURL() {
        return onesignalNotificationURL;
    }

    public void setOnesignalNotificationURL(String onesignalNotificationURL) {
        this.onesignalNotificationURL = onesignalNotificationURL;
    }

    
    public void pushNotification(String json) throws BaseException{
        
        final String methodName = "pushNotification()";
        log.info("Entering :" + methodName);

        Client client = ClientBuilder.newClient();
        String result = null;
        try {
            result = client.target(this.onesignalNotificationURL)
                    .request().accept(MediaType.APPLICATION_JSON)
                    .header(ApplicationConstants.AUTHORIZATION, this.oneSignalRestApiKey)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                   .post(Entity.entity(json, MediaType.APPLICATION_JSON), String.class);
                    
        } catch (Exception ex) {
            log.error("Exception in HTTP call :"+methodName+" - "+ex.getMessage());
            throw new BaseException("Error in service!", ex);
        }
        
        if(null == result){
            log.error("Push notification returning NULL");
        }

        //TODO : check for result = true
        log.info("Exiting :" + methodName);
    }
    
}
