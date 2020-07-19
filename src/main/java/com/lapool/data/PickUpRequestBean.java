package com.lapool.data;

import java.util.Date;
import java.util.List;

/**
 * Created by akshat
 */
public class PickUpRequestBean {

    private long authID;
    private long pickID;
    private double pickUpLat;
    private double pickUpLng;
    private double dropLat;
    private double dropLng;
    private String userID;
    private String pickAddress;
    private String dropAddress;
    private String name;
    private char gender;
    private int age;
    private boolean girlsOnly;
    private boolean smoking;
    private boolean openToAll;
    private boolean music;
    private Date created;
    private double distanceFromUser;
    private int seats;
    //List of networks the authID is related to the requested user
    private List<Integer> networkLinked;

    public double getDistanceFromUser() {
        return distanceFromUser;
    }

    public void setDistanceFromUser(double distanceFromUser) {
        this.distanceFromUser = distanceFromUser;
    }

    public long getAuthID() {
        return authID;
    }

    public void setAuthID(long authID) {
        this.authID = authID;
    }

    public long getPickID() {
        return pickID;
    }

    public void setPickID(long pickID) {
        this.pickID = pickID;
    }
    
    

    public double getPickUpLat() {
        return pickUpLat;
    }

    public void setPickUpLat(double pickUpLat) {
        this.pickUpLat = pickUpLat;
    }

    public double getPickUpLng() {
        return pickUpLng;
    }

    public void setPickUpLng(double pickUpLng) {
        this.pickUpLng = pickUpLng;
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
    

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isGirlsOnly() {
        return girlsOnly;
    }

    public void setGirlsOnly(boolean girlsOnly) {
        this.girlsOnly = girlsOnly;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }



    public List getNetworkLinked() {
        return networkLinked;
    }

    public void setNetworkLinked(List<Integer> networkLinked) {
        this.networkLinked = networkLinked;
    }

    public boolean isOpenToAll() {
        return openToAll;
    }

    public void setOpenToAll(boolean openToAll) {
        this.openToAll = openToAll;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
    
    

}
