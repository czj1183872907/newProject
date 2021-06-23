package com.chery.exeed.crm.lead.dto;

/**
 * @Auther: yueyun.pan
 * @Date: 2019/1/28 15:54
 * @Description:
 */
public class ExportLeadSearchDTO {

    private String dealer;
    private Integer leadStatus;
    private String startDateStr;
    private String endDateStr;

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public Integer getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(Integer leadStatus) {
        this.leadStatus = leadStatus;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }
}
