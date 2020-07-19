/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author akshat
 */
@Entity
@DynamicUpdate
@Table(name = "PICKUPREQUEST", catalog = "LAPOOL")
public class PickUpRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PICKID", unique = true, nullable = false)
    private long pickID;

    @Column(name = "PICKUPLAT", nullable = false)
    private double pickUpLat;

    @Column(name = "PICKUPLNG", nullable = false)
    private double pickUpLng;

    @Column(name = "DROPLAT", nullable = false)
    private double dropLat;

    @Column(name = "DROPLNG", nullable = false)
    private double dropLng;

    @Column(name = "PICKADDRESS")
    private String pickAddress;

    @Column(name = "DROPADDRESS")
    private String dropAddress;

    @Column(name = "GIRLSONLY", nullable = false)
    private int girlsOnly;

    @Column(name = "SMOKING", nullable = false)
    private int smoking;

    @Column(name = "MUSIC", nullable = false)
    private int music;
    
    @Column(name = "OPENTOALL", nullable = false)
    private int openToAll;

    @Column(name = "STATUS", nullable = false)
    private int status;
    
    @Column(name = "SEATS", nullable = false)
    private int seats;

    @Column(name = "CREATED", nullable = false, columnDefinition = "DATETIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "UPDATED", nullable = false, columnDefinition = "DATETIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date updated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID", referencedColumnName = "ID")
    private AuthProvider authProvider;
    
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "pickUpRequest", cascade = CascadeType.ALL)
    private Trip trip;

    public long getPickID() {
        return pickID;
    }

    public void setPickID(long pickID) {
        this.pickID = pickID;
    }

    public double getPickUpLat() {
        return pickUpLat;
    }

    public void setPickUpLat(double pickUpLat) {
        this.pickUpLat = pickUpLat;
    }

    public double getPickUpLng() {
        return pickUpLng;
    }

    public void setPickUpLng(double pickUpLng) {
        this.pickUpLng = pickUpLng;
    }

    public double getDropLat() {
        return dropLat;
    }

    public void setDropLat(double dropLat) {
        this.dropLat = dropLat;
    }

    public double getDropLng() {
        return dropLng;
    }

    public void setDropLng(double dropLng) {
        this.dropLng = dropLng;
    }

    public String getPickAddress() {
        return pickAddress;
    }

    public void setPickAddress(String pickAddress) {
        this.pickAddress = pickAddress;
    }

    public String getDropAddress() {
        return dropAddress;
    }

    public void setDropAddress(String dropAddress) {
        this.dropAddress = dropAddress;
    }

    public int getGirlsOnly() {
        return girlsOnly;
    }

    public void setGirlsOnly(int girlsOnly) {
        this.girlsOnly = girlsOnly;
    }

    public int getSmoking() {
        return smoking;
    }

    public void setSmoking(int smoking) {
        this.smoking = smoking;
    }



    public int getMusic() {
        return music;
    }

    public void setMusic(int music) {
        this.music = music;
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

    public void setCreated(Date createdDate) {
        this.created = createdDate;
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

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public int getOpenToAll() {
        return openToAll;
    }

    public void setOpenToAll(int openToAll) {
        this.openToAll = openToAll;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
    
    

}
