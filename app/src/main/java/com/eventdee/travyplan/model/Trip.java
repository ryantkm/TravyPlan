package com.eventdee.travyplan.model;

import java.util.Date;

public class Trip {

//    public static final String FIELD_CITY = "city";
//    public static final String FIELD_CATEGORY = "category";
//    public static final String FIELD_PRICE = "price";
//    public static final String FIELD_POPULARITY = "numRatings";
//    public static final String FIELD_AVG_RATING = "avgRating";

    private String name;
    private Date startDate;
    private Date endDate;
    private String coverPhoto;

    public Trip() {
    }

    public Trip(String name, Date startDate, Date endDate, String coverPhoto) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.coverPhoto = coverPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }
}
