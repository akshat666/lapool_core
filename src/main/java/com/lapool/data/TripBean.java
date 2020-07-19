/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.data;

import java.io.Serializable;

/**
 *
 * @author akshat666
 */
public class TripBean implements Serializable {

    private long tripID;

    private String hopperName;
    private String dropperName;

    private long tripStartTime;
    private long tripEndTime;

    private boolean isNoSmoking;
    private boolean isGirlsOnly;
    private boolean music;

    //userID of the person who requested a pickup
    private long hopperID;
    //UserID of the person who accepted to Drop a user
    private long dropperID;

    // The userIDs(FB, twitter etc login IDs
    private String hopperProviderID;
    private String dropperProviderID;

    private char hopperSex;
    private char dropperSex;
    private int hopperAge;
    private int dropperAge;

    //Pickup location when it was requested
    private double pickupLat;
    private double pickupLng;

    //Drop off location when it was requested
    private double dropLat;
    private double dropLng;

    //Dropper and hopper location when req is confirmed
    private double dropperStartLat;
    private double dropperStartLng;

    private double hopperStartLat;
    private double hopperStartLng;

    //Dropper and hopper location when they end the trip
    private double dropperStopLat;
    private double dropperStopLng;

    private double hopperStopLat;
    private double hopperStopLng;

    private String pickAddress;
    private String dropAddress;

    private int status;
    private String tripKey;
    

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHopperProviderID() {
        return hopperProviderID;
    }

    public void setHopperProviderID(String hopperProviderID) {
        this.hopperProviderID = hopperProviderID;
    }

    public String getDropperProviderID() {
        return dropperProviderID;
    }

    public void setDropperProviderID(String dropperProviderID) {
        this.dropperProviderID = dropperProviderID;
    }

    public char getHopperSex() {
        return hopperSex;
    }

    public void setHopperSex(char hopperSex) {
        this.hopperSex = hopperSex;
    }

    public char getDropperSex() {
        return dropperSex;
    }

    public void setDropperSex(char dropperSex) {
        this.dropperSex = dropperSex;
    }

    public int getHopperAge() {
        return hopperAge;
    }

    public void setHopperAge(int hopperAge) {
        this.hopperAge = hopperAge;
    }

    public int getDropperAge() {
        return dropperAge;
    }

    public void setDropperAge(int dropperAge) {
        this.dropperAge = dropperAge;
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

    public String getHopperName() {
        return hopperName;
    }

    public void setHopperName(String hopperName) {
        this.hopperName = hopperName;
    }

    public String getDropperName() {
        return dropperName;
    }

    public void setDropperName(String dropperName) {
        this.dropperName = dropperName;
    }

    public long getTripID() {
        return tripID;
    }

    public void setTripID(long tripID) {
        this.tripID = tripID;
    }

    public long getTripStartTime() {
        return tripStartTime;
    }

    public void setTripStartTime(long tripStartTime) {
        this.tripStartTime = tripStartTime;
    }

    public long getTripEndTime() {
        return tripEndTime;
    }

    public void setTripEndTime(long tripEndTime) {
        this.tripEndTime = tripEndTime;
    }

    public boolean isIsNoSmoking() {
        return isNoSmoking;
    }

    public void setIsNoSmoking(boolean isNoSmoking) {
        this.isNoSmoking = isNoSmoking;
    }

    public boolean isIsGirlsOnly() {
        return isGirlsOnly;
    }

    public void setIsGirlsOnly(boolean isGirlsOnly) {
        this.isGirlsOnly = isGirlsOnly;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public long getHopperID() {
        return hopperID;
    }

    public void setHopperID(long hopperID) {
        this.hopperID = hopperID;
    }

    public long getDropperID() {
        return dropperID;
    }

    public void setDropperID(long dropperID) {
        this.dropperID = dropperID;
    }

    public double getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(double pickupLat) {
        this.pickupLat = pickupLat;
    }

    public double getPickupLng() {
        return pickupLng;
    }

    public void setPickupLng(double pickupLng) {
        this.pickupLng = pickupLng;
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

    public double getDropperStartLat() {
        return dropperStartLat;
    }

    public void setDropperStartLat(double dropperStartLat) {
        this.dropperStartLat = dropperStartLat;
    }

    public double getDropperStartLng() {
        return dropperStartLng;
    }

    public void setDropperStartLng(double dropperStartLng) {
        this.dropperStartLng = dropperStartLng;
    }

    public double getDropperStopLat() {
        return dropperStopLat;
    }

    public void setDropperStopLat(double dropperStopLat) {
        this.dropperStopLat = dropperStopLat;
    }

    public double getDropperStopLng() {
        return dropperStopLng;
    }

    public void setDropperStopLng(double dropperStopLng) {
        this.dropperStopLng = dropperStopLng;
    }

    public double getHopperStopLat() {
        return hopperStopLat;
    }

    public void setHopperStopLat(double hopperStopLat) {
        this.hopperStopLat = hopperStopLat;
    }

    public double getHopperStopLng() {
        return hopperStopLng;
    }

    public void setHopperStopLng(double hopperStopLng) {
        this.hopperStopLng = hopperStopLng;
    }

    public double getHopperStartLat() {
        return hopperStartLat;
    }

    public void setHopperStartLat(double hopperStartLat) {
        this.hopperStartLat = hopperStartLat;
    }

    public double getHopperStartLng() {
        return hopperStartLng;
    }

    public void setHopperStartLng(double hopperStartLng) {
        this.hopperStartLng = hopperStartLng;
    }

    public String getTripKey() {
        return tripKey;
    }

    public void setTripKey(String tripKey) {
        this.tripKey = tripKey;
    }
    
    

}
