package com.chery.exeed.crm.lead.model;

import java.util.Date;

public class ActivitiesDuration {
    private Integer id;

    private String activitiesDuration;

    private String activitiesName;

    private Integer activitiesType;

    private Integer activitiesStatus;

    private Integer assignedConsultant;

    private Date acitivitiesTimePlan;

    private Date acitivitiesTimeActual;

    private Integer lead;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivitiesDuration() {
        return activitiesDuration;
    }

    public void setActivitiesDuration(String activitiesDuration) {
        this.activitiesDuration = activitiesDuration == null ? null : activitiesDuration.trim();
    }

    public String getActivitiesName() {
        return activitiesName;
    }

    public void setActivitiesName(String activitiesName) {
        this.activitiesName = activitiesName == null ? null : activitiesName.trim();
    }

    public Integer getActivitiesType() {
        return activitiesType;
    }

    public void setActivitiesType(Integer activitiesType) {
        this.activitiesType = activitiesType;
    }

    public Integer getActivitiesStatus() {
        return activitiesStatus;
    }

    public void setActivitiesStatus(Integer activitiesStatus) {
        this.activitiesStatus = activitiesStatus;
    }

    public Integer getAssignedConsultant() {
        return assignedConsultant;
    }

    public void setAssignedConsultant(Integer assignedConsultant) {
        this.assignedConsultant = assignedConsultant;
    }

    public Date getAcitivitiesTimePlan() {
        return acitivitiesTimePlan;
    }

    public void setAcitivitiesTimePlan(Date acitivitiesTimePlan) {
        this.acitivitiesTimePlan = acitivitiesTimePlan;
    }

    public Date getAcitivitiesTimeActual() {
        return acitivitiesTimeActual;
    }

    public void setAcitivitiesTimeActual(Date acitivitiesTimeActual) {
        this.acitivitiesTimeActual = acitivitiesTimeActual;
    }

    public Integer getLead() {
        return lead;
    }

    public void setLead(Integer lead) {
        this.lead = lead;
    }
}