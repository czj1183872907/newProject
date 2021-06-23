package com.chery.exeed.crm.lead.model;

import java.util.Date;

public class LeadTask {
    private Long id;

    private Integer leadId;

    private Integer taskType;

    private Integer owner;

    private Integer status;

    private Date dueDatePlan;

    private Date dueDateActual;

    private Integer taskResult;

    private String memo;

    private Date firstCallTime;

    private String nextCallTime;

    private Integer callTimes;

    private Date modifyTime;

    private Date cdrStopTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDueDatePlan() {
        return dueDatePlan;
    }

    public void setDueDatePlan(Date dueDatePlan) {
        this.dueDatePlan = dueDatePlan;
    }

    public Date getDueDateActual() {
        return dueDateActual;
    }

    public void setDueDateActual(Date dueDateActual) {
        this.dueDateActual = dueDateActual;
    }

    public Integer getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(Integer taskResult) {
        this.taskResult = taskResult;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Date getFirstCallTime() {
        return firstCallTime;
    }

    public void setFirstCallTime(Date firstCallTime) {
        this.firstCallTime = firstCallTime;
    }

    public String getNextCallTime() {
        return nextCallTime;
    }

    public void setNextCallTime(String nextCallTime) {
        this.nextCallTime = nextCallTime == null ? null : nextCallTime.trim();
    }

    public Integer getCallTimes() {
        return callTimes;
    }

    public void setCallTimes(Integer callTimes) {
        this.callTimes = callTimes;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCdrStopTime() {
        return cdrStopTime;
    }

    public void setCdrStopTime(Date cdrStopTime) {
        this.cdrStopTime = cdrStopTime;
    }
}