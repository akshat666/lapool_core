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
 * @author akshat666
 */
@Entity
@DynamicUpdate
@Table(name = "LOCATION", catalog = "LAPOOL")
public class Location implements Serializable{
    
    @Id
    @GenericGenerator(name = "generator", strategy = "foreign",
            parameters = @Parameter(name = "property", value = "authProvider"))
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", unique = true, nullable = false)
    private long id;
    
    @Column(name = "LASTLAT")
    private double lastLat;
    
    @Column(name = "LASTLNG")
    private double lastLng;
    
    @Column(name = "RADIANSLNG")
    private double radiansLng;
    
    @Column(name = "SINRADIANSLAT")
    private double sinRadiansLat;
    
    @Column(name = "COSRADIANSLAT")
    private double cosRadiansLat;
    
    @Column(name = "CREATED", nullable = false, columnDefinition = "DATETIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "UPDATED", nullable = false, columnDefinition = "DATETIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date updated;
    
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private AuthProvider authProvider;

    public double getRadiansLng() {
        return radiansLng;
    }

    public void setRadiansLng(double radiansLng) {
        this.radiansLng = radiansLng;
    }

    public double getSinRadiansLat() {
        return sinRadiansLat;
    }

    public void setSinRadiansLat(double sinRadiansLat) {
        this.sinRadiansLat = sinRadiansLat;
    }

    public double getCosRadiansLat() {
        return cosRadiansLat;
    }

    public void setCosRadiansLat(double cosRadiansLat) {
        this.cosRadiansLat = cosRadiansLat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLastLat() {
        return lastLat;
    }

    public void setLastLat(double lastLat) {
        this.lastLat = lastLat;
    }

    public double getLastLng() {
        return lastLng;
    }

    public void setLastLng(double lastLng) {
        this.lastLng = lastLng;
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
    
    
    
}
