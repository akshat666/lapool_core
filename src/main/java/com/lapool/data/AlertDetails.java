/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author akshat666
 */
public class AlertDetails implements Serializable{
    
    
    private long alerterAuthID;
    private String alertedByName;
    private Date alertedAt;
        
    private TripDetails tripDetails;
    private List<LocationDetails> alertLocations;
    private double alertAtLat;
    private double alertAtLng;

    public String getAlertedByName() {
        return alertedByName;
    }

    public void setAlertedByName(String alertedByName) {
        this.alertedByName = alertedByName;
    }

    public Date getAlertedAt() {
        return alertedAt;
    }

    public void setAlertedAt(Date alertedAt) {
        this.alertedAt = alertedAt;
    }
    

    public double getAlertAtLat() {
        return alertAtLat;
    }

    public void setAlertAtLat(double alertAtLat) {
        this.alertAtLat = alertAtLat;
    }

    public double getAlertAtLng() {
        return alertAtLng;
    }

    public void setAlertAtLng(double alertAtLng) {
        this.alertAtLng = alertAtLng;
    }

    public List<LocationDetails> getAlertLocations() {
        return alertLocations;
    }

    public void setAlertLocations(List<LocationDetails> alertLocations) {
        this.alertLocations = alertLocations;
    }

    public TripDetails getTripDetails() {
        return tripDetails;
    }

    public void setTripDetails(TripDetails tripDetails) {
        this.tripDetails = tripDetails;
    }

    public long getAlerterAuthID() {
        return alerterAuthID;
    }

    public void setAlerterAuthID(long alerterAuthID) {
        this.alerterAuthID = alerterAuthID;
    }
    
    
    
    
}
