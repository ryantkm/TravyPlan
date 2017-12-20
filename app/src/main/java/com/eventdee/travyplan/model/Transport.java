package com.eventdee.travyplan.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Transport implements Parcelable {
    private String name;

    public Transport(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    protected Transport(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Transport> CREATOR = new Parcelable.Creator<Transport>() {
        @Override
        public Transport createFromParcel(Parcel source) {
            return new Transport(source);
        }

        @Override
        public Transport[] newArray(int size) {
            return new Transport[size];
        }
    };
}
