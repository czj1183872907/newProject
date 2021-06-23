package com.chery.exeed.crm.lead.model;

public class DccRebackConfig {
    private String chanalCode;

    private String needSend;

    public String getChanalCode() {
        return chanalCode;
    }

    public void setChanalCode(String chanalCode) {
        this.chanalCode = chanalCode == null ? null : chanalCode.trim();
    }

    public String getNeedSend() {
        return needSend;
    }

    public void setNeedSend(String needSend) {
        this.needSend = needSend == null ? null : needSend.trim();
    }
}