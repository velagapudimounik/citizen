package com.drughub.citizen.model;

import org.json.JSONObject;

import io.realm.RealmObject;

public class Address extends RealmObject {
    private int addressId ;
    private Country country = new Country();
    private City city = new City();
    private State state = new State();
    private String doorNumber;
    private String buildingName;
    private String streetName;
    private String postalCode;
    private String areaCode;
    private String areaName;
    private String addressType;
    private String district;
    private String landMark;
    private double lng;
    private double lat;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public JSONObject toServiceProvider() {
        JSONObject addressObject = new JSONObject();
        try {

            if(getAddressId()>0)
            addressObject.put("addressId", getAddressId());

            addressObject.put("doorNumber", getDoorNumber());
            addressObject.put("buildingName", getBuildingName());
            addressObject.put("streetName", getStreetName());
            addressObject.put("areaCode", getAreaCode());
            addressObject.put("areaName",getAreaName());
            addressObject.put("landMark", getLandMark());
            addressObject.put("postalCode", getPostalCode());
            addressObject.put("lat", getLat());
            addressObject.put("lng", getLng());
            addressObject.put("state", getState().getValueIds());
            addressObject.put("country", getCountry().getValueIdsCode());
            addressObject.put("city", getCity().getValueIds());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return addressObject;

    }

    public String getAddressLine1() {
        // buildingName, doorNumber, streetName, areaName.
        StringBuffer s = new StringBuffer();
        if (getBuildingName() != null && !getBuildingName().isEmpty())
            s.append(getBuildingName());
        if (s.length() > 0)
            s.append(", ");
        if(getDoorNumber() != null && !getDoorNumber().isEmpty())
        s.append(getDoorNumber());
        if (s.length() > 0)
            s.append(", ");
        if(getStreetName() != null && !getStreetName().isEmpty())
        s.append(getStreetName());
        if (s.length() > 0)
            s.append(", ");
        if(getAreaName() != null && !getAreaName().isEmpty())
        s.append(getAreaName());

        return s.toString().replaceAll("null", "");
    }

    public String getAddressLine2() {
        // city, state, country, postalCode.
        StringBuffer s = new StringBuffer();
        if (getCity() != null && getCity().getValue() != null && !getCity().getValue().isEmpty())
            s.append(getCity().getValue());
        if (s.length() > 0)
            s.append(", ");
        if( getState() != null && getState().getValue() != null && !getState().getValue().isEmpty())
        s.append("" + getState().getValue());
        if (s.length() > 0)
            s.append(", ");
        if (getCountry() != null && getCountry().getValue() != null && !getCountry().getValue().isEmpty())
            s.append("" + getCountry().getValue());
        if (s.length() > 0 && !getPostalCode().isEmpty())
            s.append(", ");
        s.append(getPostalCode());

        return s.toString().replaceAll("null", "").replaceAll(",,",",");
    }

}
