/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.data;

import java.util.List;

/**
 * Holds trip details for the UI
 *
 * @author akshat666
 */
public class TripDetails extends TripBean {

    private List<LocationDetails> hopperRoute;
    private List<LocationDetails> dropperRoute;
    private String dropperPhone;
    private String hopperPhone;
    private long pickCreatedAt;
    private int hopperProviderType;
    private int dropperProviderType;


    public int getHopperProviderType() {
        return hopperProviderType;
    }

    public void setHopperProviderType(int hopperProviderType) {
        this.hopperProviderType = hopperProviderType;
    }

    public int getDropperProviderType() {
        return dropperProviderType;
    }

    public void setDropperProviderType(int dropperProviderType) {
        this.dropperProviderType = dropperProviderType;
    }
    
    
    
    public long getPickCreatedAt() {
        return pickCreatedAt;
    }

    public void setPickCreatedAt(long pickCreatedAt) {
        this.pickCreatedAt = pickCreatedAt;
    }

    public List<LocationDetails> getHopperRoute() {
        return hopperRoute;
    }

    public void setHopperRoute(List<LocationDetails> hopperRoute) {
        this.hopperRoute = hopperRoute;
    }

    public List<LocationDetails> getDropperRoute() {
        return dropperRoute;
    }

    public void setDropperRoute(List<LocationDetails> dropperRoute) {
        this.dropperRoute = dropperRoute;
    }

    public String getDropperPhone() {
        return dropperPhone;
    }

    public void setDropperPhone(String dropperPhone) {
        this.dropperPhone = dropperPhone;
    }

    public String getHopperPhone() {
        return hopperPhone;
    }

    public void setHopperPhone(String hopperPhone) {
        this.hopperPhone = hopperPhone;
    }

}
