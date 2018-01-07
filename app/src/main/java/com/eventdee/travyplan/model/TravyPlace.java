package com.eventdee.travyplan.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TravyPlace implements Parcelable {

    private Date date;
    private String id;
    private List<Integer> placeTypes;
    private String address;
    private String country;
    private String countryCode;
    private String name;
//    private GeoPoint geoPoint;
    private double latitude;
    private double longtitude;
//    private LatLngBounds viewport;
//    private GeoPoint northEastLatLngBounds;
//    private GeoPoint southWestLatLngBounds;

    private double northEastViewportLatitude;
    private double northEastViewportLongtitude;

    private double southWestViewportLatitude;
    private double southWestViewportLongtitude;

    private String websiteUri;
    private String phoneNumber;
    private float rating;
    private int priceLevel;
    private String attributions;
    private String transportMode;
    private ArrayList<String> photos;
    private String notes;

    public TravyPlace() {
    }

    public TravyPlace(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getNorthEastViewportLatitude() {
        return northEastViewportLatitude;
    }

    public void setNorthEastViewportLatitude(double northEastViewportLatitude) {
        this.northEastViewportLatitude = northEastViewportLatitude;
    }

    public double getNorthEastViewportLongtitude() {
        return northEastViewportLongtitude;
    }

    public void setNorthEastViewportLongtitude(double northEastViewportLongtitude) {
        this.northEastViewportLongtitude = northEastViewportLongtitude;
    }

    public double getSouthWestViewportLatitude() {
        return southWestViewportLatitude;
    }

    public void setSouthWestViewportLatitude(double southWestViewportLatitude) {
        this.southWestViewportLatitude = southWestViewportLatitude;
    }

    public double getSouthWestViewportLongtitude() {
        return southWestViewportLongtitude;
    }

    public void setSouthWestViewportLongtitude(double southWestViewportLongtitude) {
        this.southWestViewportLongtitude = southWestViewportLongtitude;
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

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeString(this.id);
        dest.writeList(this.placeTypes);
        dest.writeString(this.address);
        dest.writeString(this.country);
        dest.writeString(this.countryCode);
        dest.writeString(this.name);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longtitude);
        dest.writeDouble(this.northEastViewportLatitude);
        dest.writeDouble(this.northEastViewportLongtitude);
        dest.writeDouble(this.southWestViewportLatitude);
        dest.writeDouble(this.southWestViewportLongtitude);
        dest.writeString(this.websiteUri);
        dest.writeString(this.phoneNumber);
        dest.writeFloat(this.rating);
        dest.writeInt(this.priceLevel);
        dest.writeString(this.attributions);
        dest.writeString(this.transportMode);
        dest.writeStringList(this.photos);
        dest.writeString(this.notes);
    }

    protected TravyPlace(Parcel in) {
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.id = in.readString();
        this.placeTypes = new ArrayList<Integer>();
        in.readList(this.placeTypes, Integer.class.getClassLoader());
        this.address = in.readString();
        this.country = in.readString();
        this.countryCode = in.readString();
        this.name = in.readString();
        this.latitude = in.readDouble();
        this.longtitude = in.readDouble();
        this.northEastViewportLatitude = in.readDouble();
        this.northEastViewportLongtitude = in.readDouble();
        this.southWestViewportLatitude = in.readDouble();
        this.southWestViewportLongtitude = in.readDouble();
        this.websiteUri = in.readString();
        this.phoneNumber = in.readString();
        this.rating = in.readFloat();
        this.priceLevel = in.readInt();
        this.attributions = in.readString();
        this.transportMode = in.readString();
        this.photos = in.createStringArrayList();
        this.notes = in.readString();
    }

    public static final Creator<TravyPlace> CREATOR = new Creator<TravyPlace>() {
        @Override
        public TravyPlace createFromParcel(Parcel source) {
            return new TravyPlace(source);
        }

        @Override
        public TravyPlace[] newArray(int size) {
            return new TravyPlace[size];
        }
    };
}
