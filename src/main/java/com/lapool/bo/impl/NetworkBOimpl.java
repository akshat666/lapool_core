/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bo.impl;

import com.lapool.bo.NetworkBO;
import com.lapool.dao.NetworkDAO;
import com.lapool.data.EmailVerificationBean;
import com.lapool.data.UserChannelBean;
import com.lapool.exception.BaseException;
import com.lapool.model.AuthProvider;
import com.lapool.model.Channel;
import com.lapool.model.UserChannel;
import com.lapool.util.ApplicationConstants;
import com.lapool.util.MailEngine;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author akshat
 */
public class NetworkBOimpl implements NetworkBO {

    final static Logger log = LoggerFactory.getLogger(NetworkBOimpl.class);

    private MailEngine mailEngine;

    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    private String baseURL;

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    private String verifyEmailURL;

    public void setVerifyEmailURL(String verifyEmailURL) {
        this.verifyEmailURL = verifyEmailURL;
    }

    private NetworkDAO networkDAO;

    public void setNetworkDAO(NetworkDAO networkDAO) {
        this.networkDAO = networkDAO;
    }

    @Override
    @Transactional
    public void saveChannelEmail(EmailVerificationBean emailVerificationBean) throws BaseException {
        final String methodName = "saveChannelEmail()";
        log.info("Entering :" + methodName);

        String email = emailVerificationBean.getEmailId();
        long channelID = emailVerificationBean.getChannelID();
        long authID = emailVerificationBean.getAuthId();
        if (email == null || channelID == 0) {
            throw new BaseException("No Email or Code specified");
        }
        String emailDomain = email.substring(email.indexOf('@') + 1);

        if (emailDomain.length() <= 0) {
            log.error("Email format error :" + methodName);
            throw new BaseException("Invalid Email Address");
        }

        //Check if channel is enabled else return error
        Channel channelObj = networkDAO.getChannel(channelID);
        if (channelObj == null || channelObj.getStatus() == ApplicationConstants.STATUS_CHANNEL_INACTIVE) {
            log.error("Channel not enabled or present for channel ID :" + channelID);
            throw new BaseException("Channel not enabled in the system");
        }

        if (!channelObj.getEmailDomain().equalsIgnoreCase(emailDomain)) {
            log.error("Email Doamin is incorrect! " + emailDomain);
            throw new BaseException("Incorrect Email Domain.");
        }
        
        UserChannel userChannel = new UserChannel();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        userChannel.setCreated(time);
        boolean isUpdate = false;
                
        //Check if authID already has an email
        //If email present replace it with the new email
        List<UserChannel> userChannels = networkDAO.fetchUserChannels(authID);
        if (!userChannels.isEmpty()) {
            //Check if same entered email is set as inavtive
            //If yes then do nothing and throw BaseException
            for (UserChannel uc : userChannels) {
                if (uc.getEmail().equalsIgnoreCase(email) && uc.getStatus() == ApplicationConstants.STATUS_USERCHANNEL_INACTIVE) {
                    log.error(methodName+" :Email already set as inactive");
                    throw new BaseException("Email already set as inactive");
                }
                //If same email entered - do nothing
                else if(uc.getEmail().equalsIgnoreCase(email) && uc.getStatus() == ApplicationConstants.STATUS_USERCHANNEL_ACTIVE){
                    return;
                }
                //If email already in system and pending verification
                else if(uc.getEmail().equalsIgnoreCase(email) && uc.getStatus() == ApplicationConstants.STATUS_USERCHANNEL_PENDING_VERIFICATION){
                    userChannel = uc;
                    isUpdate = true;
                    break;
                }
            }
            
        }
        String veriKey = UUID.randomUUID().toString();
        
        userChannel.setEmail(email);
        userChannel.setChannel(channelObj);
        userChannel.setUpdated(time);
        userChannel.setVerificationKey(veriKey);
        userChannel.setStatus(ApplicationConstants.STATUS_USERCHANNEL_PENDING_VERIFICATION);
        AuthProvider authProvider = new AuthProvider();
        authProvider.setId(emailVerificationBean.getAuthId());

        userChannel.setAuthProvider(authProvider);
        
        //call save only if new entry
        if(!isUpdate)
        {
            networkDAO.saveChannelDetails(userChannel);
        }

        //Encode the key into UTF-8 for URL friendly chars
        try {
            veriKey = URLEncoder.encode(veriKey, ApplicationConstants.ENCODING_UTF8);
        } catch (UnsupportedEncodingException ex) {
            log.error("UnsupportedEncodingException in encoding : " + methodName + " :" + ex.getMessage());
            throw new BaseException();
        }
        //} //if already existing email send verification mail again

        String verifyURL = this.baseURL + this.verifyEmailURL + "?" + "key=" + veriKey;
        String content = ApplicationConstants.MSG_CLICK_AND_CONFIRM
                + verifyURL;
        //Send verification email
        mailEngine.sendMail(email, ApplicationConstants.STR_EMAIL_VERIFY_SUB, "User", content);

        log.info("Exiting :" + methodName);
    }

    /**
     * Returns the User's Channel details which are active or pending
     * verification
     *
     * @param authID
     * @return List
     * @throws BaseException
     */
    @Override
    @Transactional
    public List fetchUserChannelDetails(long authID) throws BaseException {
        final String methodName = "fetchUserChannelDetails()";
        log.info("Entering :" + methodName);

        List<UserChannelBean> channelBeanList = null;
        List<UserChannel> userChannels = networkDAO.fetchUserChannels(authID);

        if (userChannels != null && userChannels.size() > 0) {

            channelBeanList = new ArrayList<UserChannelBean>();
            for (UserChannel uc : userChannels) {
                //Do not load and prepare inactive user channels 
                if(uc.getStatus() == ApplicationConstants.STATUS_USERCHANNEL_INACTIVE){
                    break;
                }
                UserChannelBean userChannelBean = new UserChannelBean();
                userChannelBean.setAuthID(authID);
                //userChannelBean.setChannel(uc.getChannel());
                userChannelBean.setEmail(uc.getEmail());
                userChannelBean.setStatus(uc.getStatus());
                channelBeanList.add(userChannelBean);
            }

        }
        log.info("Exiting :" + methodName);
        return channelBeanList;
    }

    @Override
    @Transactional
    public List fetchChannels(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) throws BaseException {
        final String methodName = "fetchChannels()";
        log.info("Entering and exiting:" + methodName);
        
        return networkDAO.fetchChannels(first, pageSize, sortField, sortOrder, filters);
    }

    @Override
    @Transactional(rollbackFor = BaseException.class)
    public void saveChannel(Channel channel) throws BaseException {
        final String methodName = "saveChannel()";
        log.info("Entering and exiting:" + methodName);

        networkDAO.saveChannel(channel);

    }

    @Override
    @Transactional(rollbackFor = BaseException.class)
    public void updateChannel(Channel channel) throws BaseException {
        final String methodName = "updateChannel()";
        log.info("Entering and exiting:" + methodName);
        
        networkDAO.updateChannel(channel);
    }

    @Override
    @Transactional
    public long fetchChannelCount(Map<String, Object> filters) throws BaseException {
        final String methodName = "fetchChannelCount()";
        log.info("Entering and exiting:" + methodName);
        
        return networkDAO.fetchChannelCount(filters);
    }

    @Override
    @Transactional
    public List fetchChannelsByName(String query) throws BaseException {
        final String methodName = "fetchChannelsByName()";
        log.info("Entering and exiting:" + methodName);
        
        return networkDAO.fetchChannelsByQuery(ApplicationConstants.STR_NAME, query);
    }

}
