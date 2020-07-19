/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bean;

import com.lapool.bo.UserBO;
import com.lapool.exception.BaseException;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author akshat666
 */
@ManagedBean
@RequestScoped
public class SystemUserBean implements Serializable{
    
    private static final Logger log = LoggerFactory.getLogger(SystemUserBean.class);

    private String oldPwd;
    private String newPwd;
    
    @ManagedProperty(value="#{userBO}")
    private UserBO userBO;
    
    @ManagedProperty(value="#{sessionBean}")
    private SessionBean sessionBean;

    public void setUserBO(UserBO userBO) {
        this.userBO = userBO;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
    
    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }


    
    
    public String updatePassword(){
        final String methodName = "changePassword()";
        log.info("Entering :" + methodName);
        
        if(sessionBean == null)
        {
            log.info("Session is NULL, cannot change password :" + methodName);
            return "login?faces-redirect=true";
        }
        
        try {
            userBO.changePassword(sessionBean.getSystemUser().getAccount().getAccountID(),
                    sessionBean.getSystemUser().getUsername(), 
                    this.oldPwd, 
                    this.newPwd);
        }
        catch (BaseException ex) 
        {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error changing password", ex.getMessage()));
            return "";
        }
        
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password changed!",""));
        
        log.info("Exiting :" + methodName);
        return "";
    }
    
    public void logout(){
                final String methodName = "logout()";
        log.info("Entering :" + methodName);

        FacesContext fc = FacesContext.getCurrentInstance();

            fc.getExternalContext().getSessionMap().put("sessionBean", null);
            HttpServletRequest req = (HttpServletRequest)fc.getExternalContext().getRequest();
            req.getSession().invalidate();

            
        try {
            fc.getExternalContext().redirect(req.getContextPath() + "/pages/troofy/secure/login.xhtml");
        } catch (IOException ex) {
            log.error("Error redirecting user after logging out :" + ex.getMessage());
        }
        log.info("Exiting :"+methodName);
    }
}
