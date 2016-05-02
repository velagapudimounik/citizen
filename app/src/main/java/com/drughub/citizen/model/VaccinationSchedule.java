package com.drughub.citizen.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class VaccinationSchedule extends RealmObject {

    @PrimaryKey
    private int vaccinationScheduleId;
    private Status status;
    private Date toDate;
    private Date fromDate;
    private String cycleName;// "Cycle 1",
    private String cycleType;// "Normal",
    private int cycleNumber;// 1,
    private String vaccineName;// "BCG",
    private String indicationName;// "Bacillus Calmette–Guérin (Tuberculosis)",
    private String administeredDate;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(int cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public String getAdministeredDate() {
        return administeredDate;
    }

    public void setAdministeredDate(String administeredDate) {
        this.administeredDate = administeredDate;
    }

    public String getIndicationName() {
        return indicationName;
    }

    public void setIndicationName(String indicationName) {
        this.indicationName = indicationName;
    }

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

    public int getVaccinationScheduleId() {
        return vaccinationScheduleId;
    }

    public void setVaccinationScheduleId(int vaccinationScheduleId) {
        this.vaccinationScheduleId = vaccinationScheduleId;
    }
}
