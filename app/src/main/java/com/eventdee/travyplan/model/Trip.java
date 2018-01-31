package com.eventdee.travyplan.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.HashMap;

public class Trip implements Parcelable {

    private String name;
    private Date startDate;
    private Date endDate;
    private String coverPhoto;

    private String visibility;
    private HashMap<String, String> roles = new HashMap<String, String>();

    public Trip() {
    }

    public Trip(String name, Date startDate, Date endDate, String coverPhoto, String userid) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.coverPhoto = coverPhoto;
        this.visibility = "public";
        this.roles.put(userid, "owner");
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

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public HashMap<String, String> getRoles() {
        return roles;
    }

    public void setRoles(HashMap<String, String> roles) {
        this.roles = roles;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeLong(this.startDate != null ? this.startDate.getTime() : -1);
        dest.writeLong(this.endDate != null ? this.endDate.getTime() : -1);
        dest.writeString(this.coverPhoto);
        dest.writeString(this.visibility);
        dest.writeSerializable(this.roles);
    }

    protected Trip(Parcel in) {
        this.name = in.readString();
        long tmpStartDate = in.readLong();
        this.startDate = tmpStartDate == -1 ? null : new Date(tmpStartDate);
        long tmpEndDate = in.readLong();
        this.endDate = tmpEndDate == -1 ? null : new Date(tmpEndDate);
        this.coverPhoto = in.readString();
        this.visibility = in.readString();
        this.roles = (HashMap<String, String>) in.readSerializable();
    }

    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel source) {
            return new Trip(source);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
}
