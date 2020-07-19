/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author akshat
 */
public class ApplicationConstants {



    

    private ApplicationConstants() {
    }

    public static final int STATUS_AUTH_USER_ACTIVE = 50;
    public static final int STATUS_AUTH_USER_INACTIVE = 51;

    public static final int PROVIDER_TYPE_FACEBOOK = 100;
    public static final int PROVIDER_TYPE_TWITTER = 101;
    public static final int PROVIDER_TYPE_LINKEDIN = 102;

    public static final String SUCCESS = "SUCCESS";
    public static final String NOT_AVAILABLE = "NA";
    public static final String ID = "id";

    //Error codes
    public static final int GENERIC_SERVER_ERROR = 500;

    //FB contants
    public static final String ACCESS_TOKEN = "access_token";
    public static final String AUTHORIZATION = "Authorization";

    //Cache constants
    public static final String TOKEN = "token";
    public static final String AUTHID = "authid";

    // Pick Request status constants
    public static final int STATUS_PICKREQ_NEW = 20;
    public static final int STATUS_PICKREQ_ACCEPTED = 21;
    public static final int STATUS_PICKREQ_CANCELLED = 22;

    // Trip status constants
    public static final int STATUS_TRIP_CREATED = 50;
    @Deprecated
    public static final int STATUS_TRIP_STARTED = 51; // This status might not be used at all
    public static final int STATUS_TRIP_ENDED_BY_DROPPER = 52;
    public static final int STATUS_TRIP_ENDED_BY_HOPPER = 53;
    public static final int STATUS_TRIP_CANCELED_BY_DROPPER = 54;
    public static final int STATUS_TRIP_CANCELED_BY_HOPPER = 55;

    public static final String ENCODING_UTF8 = "UTF-8";

    public static final int PICKUPREQ_EXPIRE_TIME_IN_SECS = 900000;

    //Error messages
    public static final String MSG_TRIP_ALREADY_ACCEPTED = "Trip already accepted by another user.";
    public static final String MSG_USER_NOT_ACTIVE = "User is no longer in active state";


    //String messages
    public static final String MSG_CLICK_AND_CONFIRM = "Please click the below link and confirm your email address.\n";
    public static final String MSG_UNAUTHORIZED = "Unauthorized access";
    public static final String MSG_TRIP_NOT_ACTIVE = "Unauthorized access";

    //App error codes
    public static final int ERROR_CODE_UNAUTHORIZED = 101;
    public static int ERROR_CODE_TRIP_ALREADY_CONFIRMED = 102;
    public static int ERROR_CODE_USER_NOT_ACTIVE = 103;
    public static int ERROR_CODE_TRIP_NOT_ACTIVE = 104;
    public static int ERROR_CODE_PATHS_EXCEEDED_LIMIT = 105;

    // Push data type constants
    public static final int PUSH_TYPE_PICKREQ_CREATED = 499;
    public static final int PUSH_TYPE_PICKREQ_ACCEPTED = 500;
    public static final int PUSH_TYPE_TRIP_ENDED = 501;
    public static final int PUSH_TYPE_CHAT = 502;
    //This is for android grp message - if more than 2 notifications arrive
    public static final Map PUSH_GRP_MSG_PICKREQ;

    static {
        PUSH_GRP_MSG_PICKREQ = new HashMap<String, String>();
        PUSH_GRP_MSG_PICKREQ.put("en", "$[notif_count] people want a drop near you");
    }
    public static final Map PUSH_GRP_MSG_CHAT;

    static {
        PUSH_GRP_MSG_CHAT = new HashMap<String, String>();
        PUSH_GRP_MSG_CHAT.put("en", "$[notif_count] direct messages");
    }
    // Channel status constants
    public static final int STATUS_USERCHANNEL_PENDING_VERIFICATION = 2;
    public static final int STATUS_USERCHANNEL_ACTIVE = 0;
    public static final int STATUS_USERCHANNEL_INACTIVE = 1;

    //Channel status
    public static final int STATUS_CHANNEL_ACTIVE = 10;
    public static final int STATUS_CHANNEL_INACTIVE = 11;

    public static int USER_NETWORK_TYPE_FB = 60;

    public static final int PICKUPREQ_EXPIRE_TIME_INTERVAL = 900000;

    public static final String STR_EMAIL_VERIFY_SUB = "Verify email - [HOP OR DROP]";

    //System user status
    public static final int STATUS_SYSTEMUSER_ACTIVE = 100;
    public static final int STATUS_SYSTEMUSER_INACTIVE = 101;

    //Privileges
    public static final long PRIVILEGE_SYSTEMUSER_VIEW = 1;
    public static final long PRIVILEGE_SYSTEMUSER_ADD = 2;
    public static final long PRIVILEGE_SYSTEMUSER_EDIT = 3;
    public static final long PRIVILEGE_CHANNEL_VIEW = 4;
    public static final long PRIVILEGE_CHANNEL_ADD = 5;
    public static final long PRIVILEGE_CHANNEL_EDIT = 6;

    public static final String STR_ACTIVE = "ACTIVE";
    public static final String STR_INACTIVE = "INACTIVE";

    public static final String STR_NAME = "name";
    public static final String STR_DESCRIPTION = "description";
    public static final String POLYLINE_COLOR_GREEN = "#FFA500";
    public static final String POLYLINE_COLOR_ORANGE = "#4BED00";
    public static final String POLYLINE_COLOR_RED = "#FF0000";
    
    public static final int  NETWORK_WORK = 1;
    public static final int  NETWORK_FACEBOOK = 2;
    
    public static final int STATUS_PATH_ACTIVE = 1;
    public static final int STATUS_PATH_INACTIVE = 0;

}
