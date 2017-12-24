package com.eventdee.travyplan.model;

import com.google.android.gms.location.places.Place;

import java.util.Date;

public class TripItem {
    private Date date;
    private String type;
    private Place place;
    private Transport transport;

    public TripItem() {
    }

    public TripItem(Date date, String type, Place place) {
        this.date = date;
        this.type = type;
        this.place = place;
    }

    public TripItem(Date date, String type, Transport transport) {
        this.date = date;
        this.type = type;
        this.transport = transport;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}
