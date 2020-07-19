/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bo.impl;

import com.lapool.bo.PushBO;
import com.lapool.client.OneSignalRESTClient;
import com.lapool.data.PushNotification;
import com.lapool.exception.BaseException;
import com.lapool.util.ApplicationConstants;
import com.lapool.util.SystemUtils;
import com.owlike.genson.Genson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author akshat666
 */
public class PushBOimpl implements PushBO {

    final static Logger log = LoggerFactory.getLogger(PushBOimpl.class);

    private String oneSignalAppID;
    private OneSignalRESTClient oneSignalRESTClient;

    public void setOneSignalRESTClient(OneSignalRESTClient oneSignalRESTClient) {
        this.oneSignalRESTClient = oneSignalRESTClient;
    }

    public void setOneSignalAppID(String oneSignalAppID) {
        this.oneSignalAppID = oneSignalAppID;
    }

    @Override
    public void hopNotificationToListOfAuthIDs(List authIDs, long pickupID, String requesterName, String dropAddress) throws BaseException {
        final String methodName = "pushNotificationToListOfAuthIDs()";
        log.info("Entering :" + methodName);

        if (authIDs == null || authIDs.isEmpty()) {
            return;
        }

        // Send in a batch of 100 because only 100tags are allowed by oneSignal at a time
        // This is specific to android devices(maybe)
        //--------- start ------------
        PushNotification pushNotification = new PushNotification();
        pushNotification.setApp_id(this.oneSignalAppID);

        HashMap<String, Object> data = new HashMap<String, Object>();

        data.put("data_type", ApplicationConstants.PUSH_TYPE_PICKREQ_CREATED);
        data.put("pickupID", pickupID);
        pushNotification.setData(data);

        HashMap<String, String> headings = new HashMap<String, String>();
        headings.put("en", requesterName + " wants a HOP");

        HashMap<String, String> contents = new HashMap<String, String>();
        contents.put("en", dropAddress);

        pushNotification.setHeadings(headings);
        pushNotification.setContents(contents);
        pushNotification.setAndroid_group(String.valueOf(ApplicationConstants.PUSH_TYPE_PICKREQ_CREATED));
        pushNotification.setAndroid_group_message(ApplicationConstants.PUSH_GRP_MSG_PICKREQ);
        pushNotification.setAndroid_background_data(true);

        //Split the ids into chunks of 100 each becausae only 100 tags allowed in one request to oneSignal
        //List<Long> idList = new ArrayList<Long>(idSet);
        List<List<Long>> idChunks = SystemUtils.chopped(authIDs, 100);

        for (List<Long> list : idChunks) {
            ArrayList filterList = new ArrayList();
            for (Long id : list) {
                HashMap<String, String> filters;
                if (!filterList.isEmpty()) {
                    filters = new HashMap<String, String>();
                    filters.put("operator", "OR");
                    filterList.add(filters);
                }
                filters = new HashMap<String, String>();
                filters.put("field", "tag");
                filters.put("key", "authID");
                filters.put("relation", "=");
                filters.put("value", Long.toString(id));

                filterList.add(filters);
            }
            //
            pushNotification.setFilters(filterList);
            Genson gen = new Genson();
            oneSignalRESTClient.pushNotification(gen.serialize(pushNotification));

            log.info("Entering :" + methodName);
        }
    }

}
