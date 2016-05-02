package com.drughub.citizen.model;

import io.realm.RealmObject;

public class DateObject extends RealmObject {
    public String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
