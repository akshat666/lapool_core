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
@Table(name = "MESSAGE", catalog = "LAPOOL")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private long id;

    @Column(name = "MESSAGE", nullable = false, length = 255)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRIPID", referencedColumnName = "TRIPID")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FROMUSER", referencedColumnName = "ID")
    private AuthProvider fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TOUSER", referencedColumnName = "ID")
    private AuthProvider toUser;

    @Column(name = "CREATED", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "UPDATED", nullable = false)
    private Date updated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public AuthProvider getFromUser() {
        return fromUser;
    }

    public void setFromUser(AuthProvider fromUser) {
        this.fromUser = fromUser;
    }

    public AuthProvider getToUser() {
        return toUser;
    }

    public void setToUser(AuthProvider toUser) {
        this.toUser = toUser;
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

}
