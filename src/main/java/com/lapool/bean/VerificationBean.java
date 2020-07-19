/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bean;

import com.lapool.bo.UserBO;
import com.lapool.exception.BaseException;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.slf4j.LoggerFactory;

/**
 *
 * @author akshat
 */
@ManagedBean(name = "verification")
@RequestScoped
public class VerificationBean {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(VerificationBean.class);

    @ManagedProperty(value = "#{param.key}")
    private String key;
    private boolean valid;

    @ManagedProperty(value = "#{userBO}")
    private UserBO userBO;

    public void setUserBO(UserBO userBO) {
        this.userBO = userBO;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @PostConstruct
    public void init() {

        final String methodName = "init()";
        log.info("Entering :" + methodName);

        try {
            //First fetch user details so that we can subscribe the user after verification
//            Object[] columns = userBO.getChannelAndAuthID(key);
//            String channel = (String) columns[0];
//            long authID = (Long) columns[1];
            
            //Validate the URL link
            valid = userBO.isValidKey(this.key);

        } catch (BaseException ex) {
            log.error("Exception in verification : " + methodName + " :" + ex.getMessage());
        } catch (Exception ex) {
            log.error("Exception in verification : " + methodName + " :" + ex.getMessage());
        }
        log.info("Exiting :" + methodName);
    }

}
