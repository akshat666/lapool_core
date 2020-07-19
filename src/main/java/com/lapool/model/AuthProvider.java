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
import javax.persistence.OneToMany;
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
@Table(name = "AUTHPROVIDER", catalog = "LAPOOL")
public class AuthProvider implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private long id;

    @Column(name = "PROVIDERID", nullable = false)
    private String providerId;

    @Column(name = "PROVIDERTYPE", nullable = false)
    private int providerType;

    @Column(name = "CREATED", nullable = false, columnDefinition = "DATETIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "NAME", nullable = false, length = 250)
    private String name;

    @Column(name = "EMAIL", nullable = false, length = 250)
    private String email;

    @Column(name = "GENDER", nullable = false, length = 1)
    private char gender;

    @Column(name = "PHONE", nullable = false, length = 25)
    private String phone;

    @Column(name = "STATUS", nullable = false)
    private int status;

    @Column(name = "BIRTHDAY", nullable = false, columnDefinition = "DATETIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date birthday;

    @Column(name = "UPDATED", nullable = false, columnDefinition = "DATETIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date updated;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "authProvider", cascade = CascadeType.ALL)
    private FbUserLogin fbUserLogin;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authProvider", cascade = CascadeType.ALL)
    private Set<PickUpRequest> pickUpRequests;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authProvider")
    private Set<UserChannel> userChannels;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authProvider")
    private Set<Trip> trips;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "authProvider", cascade = CascadeType.ALL)
    private Location location;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fromUser")
    private Set<Message> fromMessages;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "toUser")
    private Set<Message> toMessages;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authProvider")
    private Set<TripLocation> tripLocations;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "authProvider", cascade = CascadeType.ALL)
    private Token token;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authProvider", cascade = CascadeType.ALL)
    private Set<Path> paths;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Set<Message> getFromMessages() {
        return fromMessages;
    }

    public void setFromMessages(Set<Message> fromMessages) {
        this.fromMessages = fromMessages;
    }

    public Set<Message> getToMessages() {
        return toMessages;
    }

    public void setToMessages(Set<Message> toMessages) {
        this.toMessages = toMessages;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public int getProviderType() {
        return providerType;
    }

    public void setProviderType(int providerType) {
        this.providerType = providerType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public FbUserLogin getFbUserLogin() {
        return fbUserLogin;
    }

    public void setFbUserLogin(FbUserLogin fbUserLogin) {
        this.fbUserLogin = fbUserLogin;
    }

    public Set<PickUpRequest> getPickUpRequests() {
        return pickUpRequests;
    }

    public void setPickUpRequests(Set<PickUpRequest> pickUpRequests) {
        this.pickUpRequests = pickUpRequests;
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

    public Set<UserChannel> getUserChannels() {
        return userChannels;
    }

    public void setUserChannels(Set<UserChannel> userChannels) {
        this.userChannels = userChannels;
    }

    public Set<Trip> getTrips() {
        return trips;
    }

    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }

    public Set<TripLocation> getTripLocations() {
        return tripLocations;
    }

    public void setTripLocations(Set<TripLocation> tripLocations) {
        this.tripLocations = tripLocations;
    }

    public Set<Path> getPaths() {
        return paths;
    }

    public void setPaths(Set<Path> paths) {
        this.paths = paths;
    }

}
