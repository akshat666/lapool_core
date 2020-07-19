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
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author akshat
 */
public class UserRestClient {

    final static Logger log = LoggerFactory.getLogger(UserRestClient.class);

    private String fbGraphURL;

    public void setFbGraphURL(String fbGraphURL) {
        this.fbGraphURL = fbGraphURL;
    }

    public String callFBGraphME(String accessToken) throws BaseException {

        final String methodName = "callFBGraphME()";
        log.info("Entering :" + methodName);

        Client client = ClientBuilder.newClient();
        String output = null;
        try {
            output = client.target(this.fbGraphURL)
                    .path("me")
                    .queryParam(ApplicationConstants.ACCESS_TOKEN, accessToken)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(String.class);
        } catch (Exception ex) {
            log.error("Exception in FB graph call :" + ex.getMessage());
            throw new BaseException("Error in service!", ex);
        }

        log.info("Exiting :" + methodName);

        return output;

    }

    public String getFBFriendListWithInstalledApp(String userId, String accessToken) throws BaseException {
        final String methodName = "getFBFriendListWithInstalledApp()";
        log.info("Entering :" + methodName);

        Client client = ClientBuilder.newClient();
        String output = null;
        try {
            output = client.target(this.fbGraphURL)
                    .path("/" + userId + "/friends")
                    .queryParam(ApplicationConstants.ACCESS_TOKEN, accessToken)
                    .queryParam("fields", "installed")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(String.class);
        } catch (Exception ex) {
            log.error("Exception in FB graph call :" + methodName + " - " + ex.getMessage());
            throw new BaseException("Error in service call!", ex);
        }

        log.info("Exiting :" + methodName);
        return output;

    }

    public String getNextFBDataSet(String nextURL) throws BaseException {
        final String methodName = "getNextFBDataSet()";
        log.info("Entering :" + methodName);

        Client client = ClientBuilder.newClient();
        String output = null;
        try {
            output = client.target(nextURL)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(String.class);
        } catch (Exception ex) {
            log.error("Exception in FB graph next data set call :" + methodName + " - " + ex.getMessage());
            throw new BaseException("Error in service call!", ex);
        }

        log.info("Exiting :" + methodName);
        return output;
    }

    public String fetchFbAllMutualFriends(String accessToken, String appSecretProof, String fbUserID) throws BaseException {
        final String methodName = "fetchFbMutualFriends()";
        log.info("Entering :" + methodName);

        Client client = ClientBuilder.newClient();
        String output = null;
        try {
            output = client.target(this.fbGraphURL)
                    .path("/" + fbUserID)
                    .queryParam(ApplicationConstants.ACCESS_TOKEN, accessToken)
                    .queryParam("fields", "context.fields(all_mutual_friends)")
                    .queryParam("appsecret_proof", appSecretProof)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(String.class);
        } catch (Exception ex) {
            log.error("Exception in FB All mutual friends API call :" + methodName + " - " + ex.getMessage());
            throw new BaseException("Error in service call!", ex);
        }
        
        log.info("Exiting :" + methodName);
        return output;
    }

}
