/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author akshat666
 */
@Entity
@DynamicUpdate
@Table(name = "TRIP", catalog = "LAPOOL")
public class Trip implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRIPID", unique = true, nullable = false)
    private Long tripID;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PICKID")
    private PickUpRequest pickUpRequest;

    @Column(name = "STATUS", nullable = false)
    private int status;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "STARTTIME")
    private Date startTime;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "STOPTIME")
    private Date stopTime;

    @Column(name = "TRIPKEY", nullable = false, unique = true)
    private String tripKey;

    @Column(name = "HOSTSTARTLNG")
    private double hostStartLng;

    @Column(name = "HOSTSTARTLAT")
    private double hostStartLat;

    @Column(name = "HOSTSTOPLNG")
    private double hostStopLng;

    @Column(name = "HOSTSTOPLAT")
    private double hostStopLat;

    @Column(name = "GUESTSTARTLNG")
    private double guestStartLng;

    @Column(name = "GUESTSTARTLAT")
    private double guestStartLat;

    @Column(name = "GUESTSTOPLNG")
    private double guestStopLng;

    @Column(name = "GUESTSTOPLAT")
    private double guestStopLat;

    @Column(name = "CREATED", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "UPDATED", nullable = false)
    private Date updated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PICKERAUTHID", referencedColumnName = "ID")
    private AuthProvider authProvider;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip")
    private Set<Message> messages;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip")
    @OrderBy("created")
    private Set<TripLocation> tripLocations;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip")
    private Set<Alert> alerts;

    public Long getTripID() {
        return tripID;
    }

    public void setTripID(Long tripID) {
        this.tripID = tripID;
    }

    public PickUpRequest getPickUpRequest() {
        return pickUpRequest;
    }

    public void setPickUpRequest(PickUpRequest pickUpRequest) {
        this.pickUpRequest = pickUpRequest;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public double getHostStartLng() {
        return hostStartLng;
    }

    public void setHostStartLng(double hostStartLng) {
        this.hostStartLng = hostStartLng;
    }

    public double getHostStartLat() {
        return hostStartLat;
    }

    public void setHostStartLat(double hostStartLat) {
        this.hostStartLat = hostStartLat;
    }

    public double getHostStopLng() {
        return hostStopLng;
    }

    public void setHostStopLng(double hostStopLng) {
        this.hostStopLng = hostStopLng;
    }

    public double getHostStopLat() {
        return hostStopLat;
    }

    public void setHostStopLat(double hostStopLat) {
        this.hostStopLat = hostStopLat;
    }

    public double getGuestStartLng() {
        return guestStartLng;
    }

    public void setGuestStartLng(double guestStartLng) {
        this.guestStartLng = guestStartLng;
    }

    public double getGuestStartLat() {
        return guestStartLat;
    }

    public void setGuestStartLat(double guestStartLat) {
        this.guestStartLat = guestStartLat;
    }

    public double getGuestStopLng() {
        return guestStopLng;
    }

    public void setGuestStopLng(double guestStopLng) {
        this.guestStopLng = guestStopLng;
    }

    public double getGuestStopLat() {
        return guestStopLat;
    }

    public void setGuestStopLat(double guestStopLat) {
        this.guestStopLat = guestStopLat;
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

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<TripLocation> getTripLocations() {
        return tripLocations;
    }

    public void setTripLocations(Set<TripLocation> tripLocations) {
        this.tripLocations = tripLocations;
    }

    public String getTripKey() {
        return tripKey;
    }

    public void setTripKey(String tripKey) {
        this.tripKey = tripKey;
    }

    public Set<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(Set<Alert> alerts) {
        this.alerts = alerts;
    }

}
