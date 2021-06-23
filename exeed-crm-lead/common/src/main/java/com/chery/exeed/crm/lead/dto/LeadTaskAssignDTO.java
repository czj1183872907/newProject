package com.chery.exeed.crm.lead.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class LeadTaskAssignDTO implements Serializable {
    private Integer owner;
    private List<Integer> ownerList;
    private Integer assignTaskCount;
    private Integer status;
    private Integer fromUserId;
    private Integer province;
    private Integer city;
    private Integer gender;
    private String searchValue;

    private String updateTimeStart;//最后更新日期区间
    private String updateTimeEnd;

    private String createTimeStart;//创建日期区间
    private String createTimeEnd;

    private List<String> channels;//来源渠道
    private List<String> phoneResult;//电话结果

    public List<Integer> getOwnerList() {
        return ownerList;
    }

    public void setOwnerList(List<Integer> ownerList) {
        this.ownerList = ownerList;
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

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public Integer getAssignTaskCount() {
        return assignTaskCount;
    }

    public void setAssignTaskCount(Integer assignTaskCount) {
        this.assignTaskCount = assignTaskCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
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
}