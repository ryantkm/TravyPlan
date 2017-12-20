package com.eventdee.travyplan.model;

import com.google.android.gms.location.places.Place;

public class TripItem {
    private String date;
    private String time;
    private String type;
    private Place place;
    private Transport transport;

    public TripItem(String date, String time, String type, Place place) {
        this.date = date;
        this.time = time;
        this.type = type;
        this.place = place;
    }

    public TripItem(String date, String time, String type, Transport transport) {
        this.date = date;
        this.time = time;
        this.type = type;
        this.transport = transport;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
