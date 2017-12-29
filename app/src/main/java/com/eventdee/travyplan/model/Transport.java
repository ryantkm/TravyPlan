package com.eventdee.travyplan.model;

import java.util.Date;

public class Transport {

    private Date date;
    private String name;
    private String itemType;

    public Transport() {
    }

    public Transport(Date date, String name) {
        this.date = date;
        this.name = name;
        itemType = "transport";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
