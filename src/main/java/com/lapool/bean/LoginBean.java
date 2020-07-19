/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bean;

import com.lapool.bo.UserBO;
import com.lapool.exception.BaseException;
import com.lapool.model.SystemUser;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.slf4j.LoggerFactory;

/**
 *
 * @author akshat666
 */
@ManagedBean
@RequestScoped
public class LoginBean {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(LoginBean.class);
    
    private Long account;
    private String userName;
    private String password;
    
    @ManagedProperty(value = "#{userBO}")
    private UserBO userBO;
    
    @ManagedProperty(value="#{sessionBean}")
    private SessionBean sessionBean;

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
    
    public void setUserBO(UserBO userBO) {
        this.userBO = userBO;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String loginUser() {
        final String methodName = "loginUser()";
        log.info("Entering :" + methodName);
        
        try {
            if (userBO.isSystemUser(account, userName, password)) 
            {
                SystemUser sysUser = userBO.fetchSystemUser(this.account, this.userName);
                sessionBean.setSystemUser(sysUser);
                return "/pages/troofy/secure/user/home?faces-redirect=true";
            }
            else 
            {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid user details", ""));
                return "";
            }
            
        } catch (BaseException ex) {
            log.error(methodName + ": Error in fetching SystemUser");
        }
        
        return "";
        
    }
}
