package com.drughub.citizen.orangeconnect;

/**
 * Created by Deepak on 3/25/2016.
 */
public class OrangeConnect {
    private String name;
    private String id;
    private String dob;
    private String time;
    private String opi;
    private String disease;
    private String administered;

    public OrangeConnect(String name, String id, String dob, String time, String opi) {
        this.name = name;
        this.id = id;
        this.dob = dob;
        this.time = time;
        this.opi = opi;
    }

    public OrangeConnect(String disease, String dob, String administered) {
        this.disease = disease;
        this.dob = dob;
        this.administered = administered;
    }

    public String getDisease() {
        return disease;
    }

    public String getAdministered() {
        return administered;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public String getId() {
        return id;
    }

    public String getOpi() {
        return opi + "%";
    }

    public String getTime() {
        return time;
    }
}