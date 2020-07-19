/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author akshat
 */
@Entity
@Table(name="CHANNEL", catalog = "LAPOOL")
public class Channel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHANNELID", unique = true, nullable = false)
    private long id;
    
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;
    
    @Column(name = "DESCRIPTION", length = 255)
    private String description;
    
    @Column(name = "STATUS", nullable = false)
    private int status;
    
    @Column(name = "EMAILDOMAIN", nullable = false, length = 255)
    private String emailDomain;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PARENTID",referencedColumnName = "CHANNELID")
    private Channel parent;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private Set<Channel> childrenChannels;

    @Column(name = "CREATED", nullable = false, columnDefinition = "DATETIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created;
    
    @Column(name = "UPDATED", nullable = false, columnDefinition = "DATETIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date updated;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "channel")
    private Set<UserChannel> userChannels;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "channel", cascade = CascadeType.ALL)
    private Set<SystemUser> systemUsers;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
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

    public Set<UserChannel> getUserChannels() {
        return userChannels;
    }

    public void setUserChannels(Set<UserChannel> userChannels) {
        this.userChannels = userChannels;
    }

    public Channel getParent() {
        return parent;
    }

    public void setParent(Channel parent) {
        this.parent = parent;
    }

    public Set<Channel> getChildrenChannels() {
        return childrenChannels;
    }

    public void setChildrenChannels(Set<Channel> childrenChannels) {
        this.childrenChannels = childrenChannels;
    }

    public Set<SystemUser> getSystemUsers() {
        return systemUsers;
    }

    public void setSystemUsers(Set<SystemUser> systemUsers) {
        this.systemUsers = systemUsers;
    }

}
