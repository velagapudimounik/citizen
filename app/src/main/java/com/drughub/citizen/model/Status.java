package com.drughub.citizen.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Status extends RealmObject {
    @PrimaryKey
    private int id;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
