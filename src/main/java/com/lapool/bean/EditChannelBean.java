/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bean;

import com.lapool.bo.NetworkBO;
import com.lapool.exception.BaseException;
import com.lapool.model.Channel;
import com.lapool.util.ApplicationConstants;
import com.lapool.util.SystemUtils;
import java.io.Serializable;
import java.sql.Time;
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
@ManagedBean
@ViewScoped
public class EditChannelBean implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(EditChannelBean.class);

    private List<Channel> channels;
    private List<String> channelStatues;
    private Channel newChannel;
    private LazyDataModel<Channel> lazyDataModel;

    @ManagedProperty(value = "#{networkBO}")
    private NetworkBO networkBO;

    public LazyDataModel<Channel> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<Channel> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public Channel getNewChannel() {
        return newChannel;
    }

    public void setNewChannel(Channel newChannel) {
        this.newChannel = newChannel;
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

    public List<String> getChannelStatues() {
        return channelStatues;
    }

    public void setChannelStatues(List<String> channelStatues) {
        this.channelStatues = channelStatues;
    }
    
    

    @PostConstruct
    public void init() {
        final String methodName = "init()";
        log.info("Entering :" + methodName);

        lazyDataModel = new LazyDataModel<Channel>() {
            @Override
            public List<Channel> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List result = null;
                try {
                    lazyDataModel.setRowCount((int) networkBO.fetchChannelCount(filters));
                    result = networkBO.fetchChannels(first, pageSize, sortField, sortOrder, filters);
                } catch (BaseException ex) {
                    log.error(methodName + ": " + ex.getMessage());
                }
                return result;
            }

        };
        channelStatues = new ArrayList<String>();
        channelStatues.add(ApplicationConstants.STR_ACTIVE);
        channelStatues.add(ApplicationConstants.STR_INACTIVE);

        newChannel = new Channel();

    }

    public void onRowEdit(RowEditEvent event) {
        final String methodName = "onRowEdit()";
        log.info("Entering :" + methodName);

        Channel channel = (Channel) event.getObject();
        if(channel.getName() == null || 
                channel.getName().isEmpty() ||
                channel.getEmailDomain() == null ||
                channel.getEmailDomain().isEmpty() ||
                channel.getDescription() == null ||
                channel.getDescription().isEmpty() ||
                channel.getStatus() <= 0){
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter valid values", "Values not entered or empty");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        return;
        }
        
        channel.setUpdated(new Timestamp(System.currentTimeMillis()));

        try {
            networkBO.updateChannel(channel);
        } catch (BaseException ex) {
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter updating values", "");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        }
    }

    public void onRowCancel(RowEditEvent event) {
        final String methodName = "onRowEdit()";
        log.info("Entering :" + methodName);

        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Channel) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void addNewChannel(ActionEvent event) {
        final String methodName = "addNewChannel()";
        log.info("Entering :" + methodName);

        long currentTime = System.currentTimeMillis();
        newChannel.setCreated(new Timestamp(currentTime));
        newChannel.setUpdated(new Time(currentTime));
        try {
            networkBO.saveChannel(newChannel);
        } catch (BaseException ex) {
            log.error(methodName + " :" + ex.getMessage());
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error saving Channel", ex.getMessage());
            FacesContext.getCurrentInstance().addMessage("growlChn", facesMsg);

        }
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Channel added", newChannel.getName() + " added successfully!");
        FacesContext.getCurrentInstance().addMessage("growl", facesMsg);
        newChannel = new Channel();
        log.info("Exiting :" + methodName);

    }
    
    public int fetchChannelStatusAsInt(String status){
        return SystemUtils.fetchChannelStatusAsInt(status);
    }
    
    public String fetchChannelStatusAsString(int status){
        return SystemUtils.fetchChannelStatusAsString(status);

    }

}
