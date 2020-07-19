/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bean;

import com.lapool.bo.PoolBO;
import com.lapool.data.LocationDetails;
import com.lapool.data.TripDetails;
import com.lapool.exception.BaseException;
import com.lapool.util.ApplicationConstants;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;
import org.slf4j.LoggerFactory;

/**
 *
 * @author akshat666
 */
@ManagedBean
@ViewScoped
public class TrackBean implements Serializable {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TrackBean.class);

    private String q;

    @ManagedProperty(value = "#{poolBO}")
    private PoolBO poolBO;

    
    private boolean isValid;
    private TripDetails tripDetails;
    private MapModel map = new DefaultMapModel();
    private Polyline hoppperLine;
    private Polyline dropperLine;
    private Marker marker;
    private String gMapKey;
    
    

    public String getgMapKey() {
        return gMapKey;
    }

    public void setgMapKey(String gMapKey) {
        this.gMapKey = gMapKey;
    }
    
    

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public MapModel getMap() {
        return map;
    }

    public void setMap(MapModel map) {
        this.map = map;
    }

    public boolean isIsValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public TripDetails getTripDetails() {
        return tripDetails;
    }

    public void setTripDetails(TripDetails tripDetails) {
        this.tripDetails = tripDetails;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public void setPoolBO(PoolBO poolBO) {
        this.poolBO = poolBO;
    }

    public void init() {

        final String methodName = "init()";
        log.info("Entering :" + methodName);

        try {

            tripDetails = poolBO.fetchTripDetailsFromKey(q);

            if (tripDetails == null) {
                log.info("No Trip matched for the given key :" + methodName);
                //isValid = false;
                redirectToError();
            }

            setUpMap();

            log.info("Exiting:" + methodName);

        } catch (BaseException ex) {
            log.error("Exception in fetching trip details : " + methodName + " :" + ex.getMessage());
            redirectToError();

        } catch (Exception ex) {
            log.error("Exception in fetching trip details : " + methodName + " :" + ex.getMessage());
            redirectToError();
        }
    }

    private void redirectToError() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) fc.getExternalContext().getRequest();

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(req.getContextPath() + "/faces/pages/troofy/error/error.xhtml");
        } catch (IOException ex) {
            log.error("Error redirecting user after Alert Trip Fetch :" + ex.getMessage());
        }
    }

    public void updateMap() {
        try {
            TripDetails trip = poolBO.fetchTripDetails(this.tripDetails.getTripID(),
                    this.tripDetails.getHopperID(),
                    this.tripDetails.getDropperID());
            this.tripDetails.setHopperRoute(trip.getHopperRoute());
            this.tripDetails.setDropperRoute(trip.getDropperRoute());
            setUpMap();
            poolBO = null;
        } catch (BaseException ex) {
            log.error("Error refreshing trip locations :" + ex.getMessage());
        }
    }

    private void setUpMap() {
        //Set up the map
        map.addOverlay(new Marker(new LatLng(tripDetails.getPickupLat(),
                tripDetails.getPickupLng()), "Pickup Point"));

        map.addOverlay(new Marker(new LatLng(tripDetails.getDropLat(),
                tripDetails.getDropLng()), "Destination"));

        hoppperLine = new Polyline();
        hoppperLine.setStrokeColor(ApplicationConstants.POLYLINE_COLOR_GREEN);
        hoppperLine.setStrokeWeight(3);

        dropperLine = new Polyline();
        dropperLine.setStrokeColor(ApplicationConstants.POLYLINE_COLOR_ORANGE);
        dropperLine.setStrokeWeight(3);

        for (LocationDetails loc : tripDetails.getHopperRoute()) {
            map.addOverlay(new Marker(new LatLng(loc.getLat(), loc.getLng()), tripDetails.getHopperName() + " - " + loc.getCreated().toString()));
            hoppperLine.getPaths().add(new LatLng(loc.getLat(), loc.getLng()));
        }

        for (LocationDetails loc : tripDetails.getDropperRoute()) {
            map.addOverlay(new Marker(new LatLng(loc.getLat(), loc.getLng()), tripDetails.getDropperName() + " - " + loc.getCreated().toString()));
            dropperLine.getPaths().add(new LatLng(loc.getLat(), loc.getLng()));
        }

        map.addOverlay(hoppperLine);
        map.addOverlay(dropperLine);
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }
}
