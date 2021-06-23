package com.chery.exeed.crm.lead.model;

import java.util.Date;

public class CustomerAuthInfo {
    private Integer id;

    private Integer customerId;

    private String password;

    private String salt;

    private String wxOpenId;

    private String wxUnionId;

    private String qqOpenId;

    private String sinaOpenId;

    private Date bindTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId == null ? null : wxOpenId.trim();
    }

    public String getWxUnionId() {
        return wxUnionId;
    }

    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId == null ? null : wxUnionId.trim();
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId == null ? null : qqOpenId.trim();
    }

    public String getSinaOpenId() {
        return sinaOpenId;
    }

    public void setSinaOpenId(String sinaOpenId) {
        this.sinaOpenId = sinaOpenId == null ? null : sinaOpenId.trim();
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }
}