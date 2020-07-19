/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.data;

import java.io.Serializable;

/**
 * This class holds values to be returned when a HOP is registered
 * @author akshat666
 */
public class HopResult implements Serializable{
    
    private long pickID;
    private long notifiedUsers;

    public long getPickID() {
        return pickID;
    }

    public void setPickID(long pickID) {
        this.pickID = pickID;
    }

    public long getNotifiedUsers() {
        return notifiedUsers;
    }

    public void setNotifiedUsers(long notifiedUsers) {
        this.notifiedUsers = notifiedUsers;
    }
    
    
    
}
