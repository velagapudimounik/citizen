package com.drughub.citizen.model;




import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DoctorConsultations extends RealmObject{
    @PrimaryKey
    private int appointmentId;
    private String patientDHCode;
    private String patientName;
    private String clinicName;
    private Date dateOfAppointment;
    private String appointmentFromTime;
    private String appointmentToTime;
    private int clinicId;
    private int isConsulationAtHome;
    private Address clinicAddress;
    private ValueIds status;
    private String vaccineName;

    public ValueIds getStatus() {
        return status;
    }

    public void setStatus(ValueIds status) {
        this.status = status;
    }

    public Address getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(Address clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

//    public boolean isConsulationAtHome() {
//        return isConsulationAtHome;
//    }
//
//    public void setIsConsulationAtHome(boolean isConsulationAtHome) {
//        this.isConsulationAtHome = isConsulationAtHome;
//    }

    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public String getAppointmentToTime() {
        return appointmentToTime;
    }

    public void setAppointmentToTime(String appointmentToTime) {
        this.appointmentToTime = appointmentToTime;
    }

    public String getAppointmentFromTime() {
        return appointmentFromTime;
    }

    public void setAppointmentFromTime(String appointmentFromTime) {
        this.appointmentFromTime = appointmentFromTime;
    }

    public Date getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(Date dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientDHCode() {
        return patientDHCode;
    }

    public void setPatientDHCode(String patientDHCode) {
        this.patientDHCode = patientDHCode;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }


    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public int isConsulationAtHome() {
        return isConsulationAtHome;
    }

    public void setIsConsulationAtHome(int isConsulationAtHome) {
        this.isConsulationAtHome = isConsulationAtHome;
    }
}
