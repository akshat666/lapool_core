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
public class ChangeStatusBean implements Serializable {

    private int status;
    private long id;
    private long authID;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAuthID() {
        return authID;
    }

    public void setAuthID(long authID) {
        this.authID = authID;
    }

    
}
