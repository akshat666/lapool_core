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
 * @author akshat666
 */
@Entity
@DynamicUpdate
@Table(name = "FEEDBACK", catalog = "LAPOOL")
public class Feedback implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FEEDBACKID", unique = true, nullable = false)
    private Long feedBackID;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TRIPID")
    private Trip trip;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID", referencedColumnName = "ID")
    private AuthProvider authProvider;
    
    @Column(name = "RATING", nullable = false)
    private int rating;
    
    @Column(name = "COMMENT", length = 1000)
    private String comment;
    
    
    @Column(name = "CREATED", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "UPDATED", nullable = false)
    private Date updated;

    public Long getFeedBackID() {
        return feedBackID;
    }

    public void setFeedBackID(Long feedBackID) {
        this.feedBackID = feedBackID;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
