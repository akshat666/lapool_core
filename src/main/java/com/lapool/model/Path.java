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
 * @author akshat666
 */
@Entity
@DynamicUpdate
@Table(name = "PATH", catalog = "LAPOOL")
public class Path implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private long id;

    @Column(name = "STARTLAT", nullable = false)
    private double startLat;

    @Column(name = "STARTLNG", nullable = false)
    private double startLng;
    
    @Column(name = "STARTRADIANSLNG")
    private double startRadiansLng;
    
    @Column(name = "STARTSINRADIANSLAT")
    private double startSinRadiansLat;
    
    @Column(name = "STARTCOSRADIANSLAT")
    private double startCosRadiansLat;

    @Column(name = "DESTLAT", nullable = false)
    private double destLat;

    @Column(name = "DESTLNG", nullable = false)
    private double destLng;

    @Column(name = "DESTRADIANSLNG")
    private double destRadiansLng;
    
    @Column(name = "DESTSINRADIANSLAT")
    private double destSinRadiansLat;
    
    @Column(name = "destCOSRADIANSLAT")
    private double destCosRadiansLat;
    
    @Column(name = "STARTADDRESS")
    private String startAddress;

    @Column(name = "DESTADDRESS")
    private String destAddress;

    @Column(name = "MON", nullable = false)
    private int mon;

    @Column(name = "TUE", nullable = false)
    private int tue;

    @Column(name = "WED", nullable = false)
    private int wed;

    @Column(name = "THU", nullable = false)
    private int thu;

    @Column(name = "FRI", nullable = false)
    private int fri;

    @Column(name = "SAT", nullable = false)
    private int sat;

    @Column(name = "SUN", nullable = false)
    private int sun;

    @Column(name = "STARTTIME", nullable = false, columnDefinition = "TIME")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date startTime;

    @Column(name = "STATUS", nullable = false)
    private int status;

    @Column(name = "CREATED", nullable = false, columnDefinition = "DATETIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "UPDATED", nullable = false, columnDefinition = "DATETIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date updated;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHID", referencedColumnName = "ID")
    private AuthProvider authProvider;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLng() {
        return startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }

    public double getDestLat() {
        return destLat;
    }

    public void setDestLat(double destLat) {
        this.destLat = destLat;
    }

    public double getDestLng() {
        return destLng;
    }

    public void setDestLng(double destLng) {
        this.destLng = destLng;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    public int getMon() {
        return mon;
    }

    public void setMon(int mon) {
        this.mon = mon;
    }

    public int getTue() {
        return tue;
    }

    public void setTue(int tue) {
        this.tue = tue;
    }

    public int getWed() {
        return wed;
    }

    public void setWed(int wed) {
        this.wed = wed;
    }

    public int getThu() {
        return thu;
    }

    public void setThu(int thu) {
        this.thu = thu;
    }

    public int getFri() {
        return fri;
    }

    public void setFri(int fri) {
        this.fri = fri;
    }

    public int getSat() {
        return sat;
    }

    public void setSat(int sat) {
        this.sat = sat;
    }

    public int getSun() {
        return sun;
    }

    public void setSun(int sun) {
        this.sun = sun;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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

    public double getStartRadiansLng() {
        return startRadiansLng;
    }

    public void setStartRadiansLng(double startRadiansLng) {
        this.startRadiansLng = startRadiansLng;
    }

    public double getStartSinRadiansLat() {
        return startSinRadiansLat;
    }

    public void setStartSinRadiansLat(double startSinRadiansLat) {
        this.startSinRadiansLat = startSinRadiansLat;
    }

    public double getStartCosRadiansLat() {
        return startCosRadiansLat;
    }

    public void setStartCosRadiansLat(double startCosRadiansLat) {
        this.startCosRadiansLat = startCosRadiansLat;
    }

    public double getDestRadiansLng() {
        return destRadiansLng;
    }

    public void setDestRadiansLng(double destRadiansLng) {
        this.destRadiansLng = destRadiansLng;
    }

    public double getDestSinRadiansLat() {
        return destSinRadiansLat;
    }

    public void setDestSinRadiansLat(double destSinRadiansLat) {
        this.destSinRadiansLat = destSinRadiansLat;
    }

    public double getDestCosRadiansLat() {
        return destCosRadiansLat;
    }

    public void setDestCosRadiansLat(double destCosRadiansLat) {
        this.destCosRadiansLat = destCosRadiansLat;
    }
    
    

}
