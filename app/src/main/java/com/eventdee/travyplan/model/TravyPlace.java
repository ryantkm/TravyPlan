package com.eventdee.travyplan.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.List;

public class TravyPlace {

    private Date date;
    private String type;
    private String itemType;
    private String id;
    private List<Integer> placeTypes;
    private String address;
    private String country;
    private String countryCode;
    private String name;
    private GeoPoint geoPoint;
//    private LatLngBounds viewport;
    private GeoPoint northEastLatLngBounds;
    private GeoPoint southWestLatLngBounds;
    private String websiteUri;
    private String phoneNumber;
    private float rating;
    private int priceLevel;
    private String attributions;
    private String transportMode;

    public TravyPlace() {
    }

    public TravyPlace(Date date, String type) {
        this.date = date;
        this.type = type;
        itemType = "place";
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

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getPlaceTypes() {
        return placeTypes;
    }

    public void setPlaceTypes(List<Integer> placeTypes) {
        this.placeTypes = placeTypes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public GeoPoint getNorthEastLatLngBounds() {
        return northEastLatLngBounds;
    }

    public void setNorthEastLatLngBounds(GeoPoint northEastLatLngBounds) {
        this.northEastLatLngBounds = northEastLatLngBounds;
    }

    public GeoPoint getSouthWestLatLngBounds() {
        return southWestLatLngBounds;
    }

    public void setSouthWestLatLngBounds(GeoPoint southWestLatLngBounds) {
        this.southWestLatLngBounds = southWestLatLngBounds;
    }

    public String getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(String websiteUri) {
        this.websiteUri = websiteUri;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }

    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }
}
