package com.chery.exeed.crm.lead.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.auth0.jwt.internal.com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 外呼黑名单
 */
public class OutCallBlackListDTO {
    private Integer id;//黑名单编号
    private String phone;//黑名单号码
    private String operateName;//操作人
    private String operateTime;//操作时间
    private String remarks;//备注

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
