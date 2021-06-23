package com.chery.exeed.crm.lead.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class LeadTaskParamDTO implements Serializable {
    private Long id;

    private Integer pageNo;
    private Integer pageSize;

    private Integer leadId;

    private Integer taskType;

    private String taskTypeDesc;

    private Integer owner;

    private String searchCode;

    private String searchValue;

    private Integer status;

    private String statusDesc;

    private Date dueDatePlan;

    private Date dueDateActual;

    private Integer taskResult;

    private String taskResultDesc;

    private Integer callTimes;

    private Integer province;

    private Integer city;

    private Integer gender;

    private String phone;

    private String memo;

    private String updateTimeStart;//最后更新日期区间
    private String updateTimeEnd;

    private String cdrStopTimeStart;//上次拨打区间
    private String cdrStopTimeEnd;

    private String createTimeStart;//创建日期区间
    private String createTimeEnd;

    private List<String> channels;//来源渠道
    private List<String> phoneResult;//电话结果

    private Integer callTimeSort;//根据上次拨打时间排序

    public Integer getCallTimeSort() {
        return callTimeSort;
    }

    public void setCallTimeSort(Integer callTimeSort) {
        this.callTimeSort = callTimeSort;
    }

    public String getUpdateTimeStart() {
        return updateTimeStart;
    }

    public void setUpdateTimeStart(String updateTimeStart) {
        this.updateTimeStart = updateTimeStart;
    }

    public String getUpdateTimeEnd() {
        return updateTimeEnd;
    }

    public void setUpdateTimeEnd(String updateTimeEnd) {
        this.updateTimeEnd = updateTimeEnd;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public List<String> getPhoneResult() {
        return phoneResult;
    }

    public void setPhoneResult(List<String> phoneResult) {
        this.phoneResult = phoneResult;
    }

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

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public Integer getPageNo() {
        return pageNo==null?1:pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize==null?20:pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getTaskTypeDesc() {
        return taskTypeDesc;
    }

    public void setTaskTypeDesc(String taskTypeDesc) {
        this.taskTypeDesc = taskTypeDesc;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getTaskResultDesc() {
        return taskResultDesc;
    }

    public void setTaskResultDesc(String taskResultDesc) {
        this.taskResultDesc = taskResultDesc;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSearchCode() {
        return searchCode;
    }

    public void setSearchCode(String searchCode) {
        this.searchCode = searchCode;
    }

    public Integer getCallTimes() {
        return callTimes;
    }

    public void setCallTimes(Integer callTimes) {
        this.callTimes = callTimes;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getCdrStopTimeStart() {
        return cdrStopTimeStart;
    }

    public void setCdrStopTimeStart(String cdrStopTimeStart) {
        this.cdrStopTimeStart = cdrStopTimeStart;
    }

    public String getCdrStopTimeEnd() {
        return cdrStopTimeEnd;
    }

    public void setCdrStopTimeEnd(String cdrStopTimeEnd) {
        this.cdrStopTimeEnd = cdrStopTimeEnd;
    }
}