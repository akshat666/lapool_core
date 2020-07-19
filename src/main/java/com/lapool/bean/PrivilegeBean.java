/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bean;

import com.lapool.model.Privilege;
import com.lapool.util.ApplicationConstants;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author akshat666
 */
@ManagedBean
@RequestScoped
public class PrivilegeBean {

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    
    public boolean isViewSystemUserPriviledged() {
        for( Privilege pri : sessionBean.getSystemUser().getAccount().getPrivileges()){
            if(pri.getPrivilegeID() == ApplicationConstants.PRIVILEGE_SYSTEMUSER_VIEW){
                return true;
            }
        }
        return false;
    }
    
        public boolean isViewChannelPriviledged() {
        for( Privilege pri : sessionBean.getSystemUser().getAccount().getPrivileges()){
            if(pri.getPrivilegeID() == ApplicationConstants.PRIVILEGE_CHANNEL_VIEW){
                return true;
            }
        }
        return false;
    }

}
