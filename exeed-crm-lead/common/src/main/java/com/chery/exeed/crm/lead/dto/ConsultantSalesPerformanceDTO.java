package com.chery.exeed.crm.lead.dto;

import java.util.Map;

public class ConsultantSalesPerformanceDTO {
    private Integer userId;
    private Integer userStatus;
    private String userName;
    private Integer newLead;
    private Integer followedLead;
    private Integer arrival;
    private Integer predict;
    private Integer order;
    private Integer win;
    private Integer lose;
    private String followupRate;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getNewLead() {
        return newLead;
    }

    public void setNewLead(Integer newLead) {
        this.newLead = newLead;
    }

    public Integer getFollowedLead() {
        return followedLead;
    }

    public void setFollowedLead(Integer followedLead) {
        this.followedLead = followedLead;
    }

    public Integer getArrival() {
        return arrival;
    }

    public void setArrival(Integer arrival) {
        this.arrival = arrival;
    }

    public Integer getPredict() {
        return predict;
    }

    public void setPredict(Integer predict) {
        this.predict = predict;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getWin() {
        return win;
    }

    public void setWin(Integer win) {
        this.win = win;
    }

    public Integer getLose() {
        return lose;
    }

    public void setLose(Integer lose) {
        this.lose = lose;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public String getFollowupRate() {
        return followupRate;
    }

    public void setFollowupRate(String followupRate) {
        this.followupRate = followupRate;
    }
}
