package com.drughub.citizen.model;

import android.util.Log;

import com.drughub.citizen.network.Globals;
import com.drughub.citizen.network.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SearchPatient extends RealmObject {


    @PrimaryKey
    private int patientUserId;
    private int parentUserId;
    private String patientName;
    private String dateOfBirth;
    private Date dateofbirth;
    private String timeOfBirth;
    private String relationName;
    private boolean isParentExists;
    private String parentDHCode, patientDHCode;
    private String parentName;
    private String parentMobile;
    private String parentEmail;
    private ValueIds gender;

    public ValueIds getGender() {
        return gender;
    }

    public void setGender(ValueIds gender) {
        this.gender = gender;
    }



    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentDHCode() {
        return parentDHCode;
    }

    public void setParentDHCode(String parentDHCode) {
        this.parentDHCode = parentDHCode;
    }

    public boolean isParentExists() {
        return isParentExists;
    }

    public void setIsParentExists(boolean isParentExists) {
        this.isParentExists = isParentExists;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getTimeOfBirth() {
        return timeOfBirth;
    }

    public void setTimeOfBirth(String timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {


        this.dateOfBirth = dateOfBirth;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String toAddPatient()
    {
        JSONObject object = new JSONObject();
        JSONObject genderObject = new JSONObject();
        try {
            object.put("patientName",getPatientName());
            object.put("dateOfBirth",getDateOfBirth());
            object.put("timeOfBirth",getTimeOfBirth());
            object.put("relationName",getRelationName());
            object.put("parentName",getParentName());
            object.put("parentMobile",getParentMobile());
            object.put("parentEmail",getParentEmail());
            if(getGender() != null)
              object.put("gender",getGender().getValueIds());
            Log.i("patientRequest",object.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();
    }

    public void AddPatient(final Globals.VolleyCallback callback)
    {
        Globals.POST(Urls.PATIENTS, toAddPatient(), new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFail(String result) {
                callback.onFail(result);

            }
        },"Add Patient");
    }

    public int getPatientUserId() {
        return patientUserId;
    }

    public void setPatientUserId(int patientUserId) {
        this.patientUserId = patientUserId;
    }

    public String getPatientDHCode() {
        return patientDHCode;
    }

    public void setPatientDHCode(String patientDHCode) {
        this.patientDHCode = patientDHCode;
    }

    public int getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(int parentUserId) {
        this.parentUserId = parentUserId;
    }

    public String getParentMobile() {
        return parentMobile;
    }

    public void setParentMobile(String parentMobile) {
        this.parentMobile = parentMobile;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }
}
