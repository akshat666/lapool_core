/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Map;

/**
 *
 * @author akshat
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PushNotification {

    private String app_id;
    private Map headings;
    private Map contents;
    private List tags; // This is depricated in 3.0v of OneSignal 
    private List filters;
    private Map data;
    //Set to true if you want the broadcast receiver to catch the notification
    private boolean android_background_data;
    // All Android notifications with the same group will be stacked together using Android's Notification Stacking feature.
    private String android_group;
    // Summary message to display when 2+ notifications are stacked together. Default is "# new messages".
    private Map android_group_message;

    public Map getHeadings() {
        return headings;
    }

    public void setHeadings(Map headings) {
        this.headings = headings;
    }

    public String getAndroid_group() {
        return android_group;
    }

    public void setAndroid_group(String android_group) {
        this.android_group = android_group;
    }

    public Map getAndroid_group_message() {
        return android_group_message;
    }

    public void setAndroid_group_message(Map android_group_message) {
        this.android_group_message = android_group_message;
    }

    public boolean isAndroid_background_data() {
        return android_background_data;
    }

    public void setAndroid_background_data(boolean android_background_data) {
        this.android_background_data = android_background_data;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public Map getContents() {
        return contents;
    }

    public void setContents(Map contents) {
        this.contents = contents;
    }

    public List getTags() {
        return tags;
    }

    public void setTags(List tags) {
        this.tags = tags;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    public List getFilters() {
        return filters;
    }

    public void setFilters(List filters) {
        this.filters = filters;
    }
    
    

}
