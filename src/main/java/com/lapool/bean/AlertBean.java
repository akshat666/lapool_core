/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bean;

import com.lapool.bo.PoolExtBO;
import com.lapool.data.AlertDetails;
import com.lapool.data.LocationDetails;
import com.lapool.exception.BaseException;
import com.lapool.util.ApplicationConstants;
import java.io.IOException;
import java.util.Properties;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
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
public class AlertBean {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AlertBean.class);

    @ManagedProperty(value = "#{poolExtBO}")
    private PoolExtBO poolExtBO;

    private String q;
    private MapModel map = new DefaultMapModel();
    private Polyline hoppperLine;
    private Polyline dropperLine;
    private Polyline alertLine;
    private Marker marker;
    private AlertDetails alertDetails;
    private String alertedBy;


    public Polyline getAlertLine() {
        return alertLine;
    }

    public void setAlertLine(Polyline alertLine) {
        this.alertLine = alertLine;
    }

    public MapModel getMap() {
        return map;
    }

    public void setMap(MapModel map) {
        this.map = map;
    }

    public Polyline getHoppperLine() {
        return hoppperLine;
    }

    public void setHoppperLine(Polyline hoppperLine) {
        this.hoppperLine = hoppperLine;
    }

    public Polyline getDropperLine() {
        return dropperLine;
    }

    public void setDropperLine(Polyline dropperLine) {
        this.dropperLine = dropperLine;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getAlertedBy() {
        return alertedBy;
    }

    public void setAlertedBy(String alertedBy) {
        this.alertedBy = alertedBy;
    }

    public AlertDetails getAlertDetails() {
        return alertDetails;
    }

    public void setAlertDetails(AlertDetails alertDetails) {
        this.alertDetails = alertDetails;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public void setPoolExtBO(PoolExtBO poolExtBO) {
        this.poolExtBO = poolExtBO;
    }

    public void init() {

        final String methodName = "init()";
        log.info("Entering :" + methodName);

        try {
            alertDetails = poolExtBO.fetchAlertDetailsFromKey(q);

            if (alertDetails == null) {
                log.info("No Alert matched for the given key :" + methodName);
                redirectToError();
            }

            setUpMap();

            log.info("Exiting:" + methodName);

        } catch (BaseException ex) {
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

    private void setUpMap() {
        //Set up the map

        alertLine = new Polyline();
        alertLine.setStrokeColor(ApplicationConstants.POLYLINE_COLOR_RED);
        alertLine.setStrokeWeight(3);

        for (LocationDetails loc : alertDetails.getAlertLocations()) {
            map.addOverlay(new Marker(new LatLng(loc.getLat(), loc.getLng()), alertDetails.getAlertedByName() + " - " + loc.getCreated().toString()));
            alertLine.getPaths().add(new LatLng(loc.getLat(), loc.getLng()));
        }

        if (alertDetails.getTripDetails() != null) {
            map.addOverlay(new Marker(new LatLng(alertDetails.getTripDetails().getPickupLat(),
                    alertDetails.getTripDetails().getPickupLng()), "Pickup Point"));

            map.addOverlay(new Marker(new LatLng(alertDetails.getTripDetails().getDropLat(),
                    alertDetails.getTripDetails().getDropLng()), "Destination"));

            hoppperLine = new Polyline();
            hoppperLine.setStrokeColor(ApplicationConstants.POLYLINE_COLOR_GREEN);
            hoppperLine.setStrokeWeight(3);

            dropperLine = new Polyline();
            dropperLine.setStrokeColor(ApplicationConstants.POLYLINE_COLOR_ORANGE);
            dropperLine.setStrokeWeight(3);

            for (LocationDetails loc : alertDetails.getTripDetails().getHopperRoute()) {
                map.addOverlay(new Marker(new LatLng(loc.getLat(), loc.getLng()), alertDetails.getTripDetails().getHopperName() + " - " + loc.getCreated().toString()));
                hoppperLine.getPaths().add(new LatLng(loc.getLat(), loc.getLng()));
            }

            for (LocationDetails loc : alertDetails.getTripDetails().getDropperRoute()) {
                map.addOverlay(new Marker(new LatLng(loc.getLat(), loc.getLng()), alertDetails.getTripDetails().getDropperName() + " - " + loc.getCreated().toString()));
                dropperLine.getPaths().add(new LatLng(loc.getLat(), loc.getLng()));
            }
            map.addOverlay(hoppperLine);
            map.addOverlay(dropperLine);
        }

        map.addOverlay(alertLine);
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }
    
        public void updateMap() {

            init();

    }

}
