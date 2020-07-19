/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bean;

import com.lapool.bo.NetworkBO;
import com.lapool.bo.SystemBO;
import com.lapool.bo.UserBO;
import com.lapool.exception.BaseException;
import com.lapool.model.Account;
import com.lapool.model.Channel;
import com.lapool.model.SystemUser;
import com.lapool.util.ApplicationConstants;
import com.lapool.util.SystemUtils;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author akshat666
 */
@ManagedBean(name = "editSystemUserBean")
@ViewScoped
public class EditSystemUserBean {

    private static final Logger log = LoggerFactory.getLogger(EditChannelBean.class);

    private LazyDataModel<SystemUser> lazyDataModel;
    private SystemUser newSystemUser;
    private List userStatues;
    private List<Channel> channels;
    private List<Account> accounts;
    private Account newUserAccount;
    private Channel newUserChannel;

    @ManagedProperty(value = "#{userBO}")
    private UserBO userBO;

    @ManagedProperty(value = "#{networkBO}")
    private NetworkBO networkBO;

    @ManagedProperty(value = "#{systemBO}")
    private SystemBO systemBO;

    public void setSystemBO(SystemBO systemBO) {
        this.systemBO = systemBO;
    }

    public Account getNewUserAccount() {
        return newUserAccount;
    }

    public void setNewUserAccount(Account newUserAccount) {
        this.newUserAccount = newUserAccount;
    }

    public NetworkBO getNetworkBO() {
        return networkBO;
    }

    public void setNetworkBO(NetworkBO networkBO) {
        this.networkBO = networkBO;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void setUserBO(UserBO userBO) {
        this.userBO = userBO;
    }

    public List getUserStatues() {
        return userStatues;
    }

    public void setUserStatues(List userStatues) {
        this.userStatues = userStatues;
    }

    public LazyDataModel<SystemUser> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<SystemUser> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public SystemUser getNewSystemUser() {
        return newSystemUser;
    }

    public void setNewSystemUser(SystemUser newSystemUser) {
        this.newSystemUser = newSystemUser;
    }

    public Channel getNewUserChannel() {
        return newUserChannel;
    }

    public void setNewUserChannel(Channel newUserChannel) {
        this.newUserChannel = newUserChannel;
    }

    @PostConstruct
    public void init() {
        final String methodName = "init()";
        log.info("Entering :" + methodName);

        lazyDataModel = new LazyDataModel<SystemUser>() {
            @Override
            public List<SystemUser> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List result = null;
                try {
                    lazyDataModel.setRowCount((int) userBO.fetchSystemUserCount(filters));
                    result = userBO.fetchSystemUsers(first, pageSize, sortField, sortOrder, filters);
                } catch (BaseException ex) {
                    log.error(methodName + ": " + ex.getMessage());
                }
                return result;
            }
        };

        userStatues = new ArrayList<String>();
        userStatues.add(ApplicationConstants.STR_ACTIVE);
        userStatues.add(ApplicationConstants.STR_INACTIVE);

        newSystemUser = new SystemUser();
    }

    public List<Channel> completeText(String query) {

        //Load Channels
        try {
            channels = networkBO.fetchChannelsByName(query);
        } catch (BaseException be) {
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error fetching Channels", "");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        }

        return channels;
    }

    public List<Account> completeAccText(String query) {
        //Load Channels
        try {
            accounts = systemBO.fetchAccountByDescription(query);
        } catch (BaseException be) {
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error fetching Accounts", "");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        }

        return accounts;
    }

    public void onRowEdit(RowEditEvent event) {
        final String methodName = "onRowEdit()";
        log.info("Entering :" + methodName);

    }

    public void onRowCancel(RowEditEvent event) {
        final String methodName = "onRowEdit()";
        log.info("Entering :" + methodName);
        
        
    }

    public void addNewSystemUser(ActionEvent event) {
        final String methodName = "addNewSystemUser()";
        log.info("Entering :" + methodName);

        newSystemUser.setPassword(SystemUtils.hashPassword(newSystemUser.getPassword()));

        long currentTime = System.currentTimeMillis();
        newSystemUser.setCreated(new Timestamp(currentTime));
        newSystemUser.setUpdated(new Timestamp(currentTime));
        newSystemUser.setAccount(newUserAccount);
        newSystemUser.setChannel(newUserChannel);
        try {
            systemBO.saveSystemUser(newSystemUser);
        } catch (BaseException ex) {
            log.error(methodName + " :" + ex.getMessage());
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error creating new user", ex.getMessage());
            FacesContext.getCurrentInstance().addMessage("growl", facesMsg);

        }

    }

    public int fetchSysUserStatusAsInt(String status) {
        return SystemUtils.fetchSysUserStatusAsInt(status);
    }
    
    public String fetchSysUserStatusAsString(int status) {
        return SystemUtils.fetchSysUserStatusAsString(status);
    }

}
