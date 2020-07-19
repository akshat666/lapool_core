/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author akshat
 */
@Entity
@DynamicUpdate
@Table(name = "USERCHANNEL", catalog = "LAPOOL")
public class UserChannel implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USERCHANNELID", unique = true, nullable = false)
    private long channelID;
    
    @Column(name = "EMAIL", nullable = false, length = 255,unique = true)
    private String email;
    
    @Column(name = "VERIFICATIONKEY", length = 255, unique = true,nullable = true)
    private String verificationKey;
    
    @Column(name = "STATUS", nullable = false)
    private int status;
    
    @Column(name = "DESCRIPTION", length = 250)
    private int description;
    
    @Column(name = "CREATED", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created;
    
    @Column(name = "UPDATED", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date updated;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID", referencedColumnName = "ID")
    private AuthProvider authProvider;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHANNELID", referencedColumnName = "CHANNELID")
    private Channel channel;

    public long getChannelID() {
        return channelID;
    }

    public void setChannelID(long channelID) {
        this.channelID = channelID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationKey() {
        return verificationKey;
    }

    public void setVerificationKey(String verificationKey) {
        this.verificationKey = verificationKey;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }
    
    
    
    
    
}
