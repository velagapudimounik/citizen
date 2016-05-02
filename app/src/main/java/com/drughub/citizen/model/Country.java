package com.drughub.citizen.model;


import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Country extends RealmObject {
    private String value;
    private String code;
    @PrimaryKey
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public JSONObject getValueIdsCode() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", getId());
            object.put("value", getValue());
            object.put("code", getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
