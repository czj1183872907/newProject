package com.chery.exeed.crm.lead.model;

import java.util.Date;

public class CustomerDealer extends CustomerDealerKey {
    private Integer sendFlag;

    private Date createTime;

    public Integer getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(Integer sendFlag) {
        this.sendFlag = sendFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}