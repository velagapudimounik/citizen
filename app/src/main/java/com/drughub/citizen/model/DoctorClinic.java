package com.drughub.citizen.model;

import android.util.Log;

import com.drughub.citizen.network.Globals;
import com.drughub.citizen.network.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class DoctorClinic extends RealmObject {
    @PrimaryKey
    private int clinicId;
    private String clinicTimings;
    private int consultationFee;
    private String clinicName;
    private String phoneNo;
    private boolean isConsultationAtHome;
    private Address address;
    private String website;
    private int yearOfEstablishment;

    public int getYearOfEstablishment() {
        return yearOfEstablishment;
    }

    public void setYearOfEstablishment(int yearOfEstablishment) {
        this.yearOfEstablishment = yearOfEstablishment;
    }


    //GetAllClinicsCalender gacc=new GetAllClinicsCalender();
    public String getClinicTimings() {
        return clinicTimings;
    }

    public void setClinicTimings(String clinicTimings) {
        this.clinicTimings = clinicTimings;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }




    public int getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(int consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String toAddClinic() {
        JSONObject object = new JSONObject();
        JSONObject address_object = new JSONObject();
        try {
            object.put("consultationFee", getConsultationFee());
            object.put("clinicName", getClinicName());
//            object.put("mobile", getPhoneNo());
            object.put("isConsultationAtHome", getIsConsultationAtHome());
            object.put("yearOfEstablishment",getYearOfEstablishment());
            object.put("website",getWebsite());

            object.put("address", getFullAddress());
            Log.i("response_addclinic",object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
    public String toUpdateClinic() {
        JSONObject object = new JSONObject();
        JSONObject address_object = new JSONObject();
        try {
            object.put("clinicId",getClinicId());
            object.put("consultationFee", getConsultationFee());
            object.put("clinicName", getClinicName());
//            object.put("mobile", getPhoneNo());
            object.put("isConsultationAtHome", getIsConsultationAtHome());
            object.put("yearOfEstablishment",getYearOfEstablishment());
            object.put("website",getWebsite());

            object.put("address", getFullAddress());
            Log.i("response_toupdateclinic",object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public void AddClinic( final Globals.VolleyCallback callback) {

        Globals.POST(Urls.CLINIC, toAddClinic(), new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    callback.onSuccess(result);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String result) {
                callback.onFail(result);
            }
        }, "Add Clinic");
    }

    public void UpdateClinic(final Globals.VolleyCallback callback ) {

        Globals.PUT(Urls.CLINIC, toUpdateClinic(), new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                callback.onSuccess(result);

            }

            @Override
            public void onFail(String result) {
                callback.onFail(result);
            }
        }, "Updating Clinic");

    }
    public void DeleteClinic(int id , final Globals.VolleyCallback callback)
    {
        Globals.DELETE(Urls.CLINIC +"/"+ id, null, new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.i("Deleted_","Successfully");
                callback.onSuccess(result);
            }

            @Override
            public void onFail(String result) {
                callback.onFail(result);
            }
        }, "Deleting Clinic");
    }


    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public JSONObject getFullAddress() {
        return address.toServiceProvider();
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public boolean getIsConsultationAtHome() {
        return isConsultationAtHome;
    }

    public void setIsConsultationAtHome(boolean isConsultationAtHome) {
        this.isConsultationAtHome = isConsultationAtHome;
    }

    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }
}
