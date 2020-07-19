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
public class UserCurrentStatus implements Serializable{
    
    private long pickReqID;
    private int pickReqStatus;
    private long tripID;
    private int tripStatus;
    private int userStatus;

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public long getPickReqID() {
        return pickReqID;
    }

    public void setPickReqID(long pickReqID) {
        this.pickReqID = pickReqID;
    }

    public int getPickReqStatus() {
        return pickReqStatus;
    }

    public void setPickReqStatus(int pickReqStatus) {
        this.pickReqStatus = pickReqStatus;
    }

    public long getTripID() {
        return tripID;
    }

    public void setTripID(long tripID) {
        this.tripID = tripID;
    }

    public int getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(int tripStatus) {
        this.tripStatus = tripStatus;
    }
    
    
    
}
