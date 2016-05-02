package com.drughub.citizen.patientrecords;

public class OutPatientPrescription {
    private String name;
    private String disease;
    private String hospital;
    private String date;

    public OutPatientPrescription(String name, String disease, String hospital, String date) {
        this.name = name;
        this.disease = disease;
        this.hospital = hospital;
        this.date = date;
    }


    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDisease() {
        return disease;
    }

    public String getHospital() {
        return hospital;
    }
}
