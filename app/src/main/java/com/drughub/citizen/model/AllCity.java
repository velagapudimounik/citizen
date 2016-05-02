package com.drughub.citizen.model;

import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AllCity extends RealmObject {
    private String value;
    @PrimaryKey
    private int id;
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
    public JSONObject getValueIds() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", getId());
            object.put("value", getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

}
