/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author akshat
 */
@Entity
@DynamicUpdate
@Table(name = "FBUSERLOGIN", catalog = "LAPOOL")
public class FbUserLogin implements Serializable {

    @Id
    @GenericGenerator(name = "generator", strategy = "foreign",
            parameters = @Parameter(name = "property", value = "authProvider"))
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", unique = true, nullable = false)
    private long id;
    
    @Column(name = "APPLICATIONID", nullable = false, length = 50)
    private String applicationID;
    
    @Column(name = "LAST_REFRESH", nullable = false, columnDefinition = "DATETIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastRefresh;
    
    @Column(name = "TOKEN", nullable = false, length = 500)
    private String token;
    
    @Column(name = "SOURCE", nullable = false, length = 100)
    private String source;
    
    @Column(name = "EXPIRES", nullable = false, columnDefinition = "DATETIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date expires;
    
    @Column(name = "CREATED", nullable = false, columnDefinition = "DATETIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created;
    
    @Column(name = "UPDATED", nullable = false, columnDefinition = "DATETIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date updated;
    
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private AuthProvider authProvider;

    
        public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getApplicationID() {
        return applicationID;
    }


    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Timestamp lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date createdDate) {
        this.created = createdDate;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updatedDate) {
        this.updated = updatedDate;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
    
    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

}
