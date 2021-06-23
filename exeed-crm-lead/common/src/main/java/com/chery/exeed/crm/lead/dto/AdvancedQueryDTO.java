package com.chery.exeed.crm.lead.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/4/22 18:48
 * @Description:
 */
public class AdvancedQueryDTO implements Serializable {
    private Integer pageNo;
    private Integer pageSize;

    private String currentDealer;
    private String startTime;//创建/导入时间，开始时间
    private String endTime;//创建/导入时间，结束时间
    private List<String> channels;//多个渠道
    private List<String> dealers;//多个经销商
    private String createName;// 创建者/导入者


    private String leadNumber;// 线索编号
    private String keyword;   // 关键字
    private List<Integer> leadStatus;//线索状态
    private String modifyStartTime;//最后更新时间，开始时间
    private String modifyEndTime;//最后更新时间，结束时间
    private String description;// 线索备注

    private String sendStart; // 下发时间，开始时间
    private String sendEnd; // 下发时间，结束时间
    private List<String> seriesName; // 意向车型
    private List<String> level; // 线索分级
    private List<String> actCode; // 活动编码

    private String winStart;
    private String winEnd;

    private String isArrival;

    public String getIsArrival() {
        return isArrival;
    }

    public void setIsArrival(String isArrival) {
        this.isArrival = isArrival;
    }

    public String getWinStart() {
        return winStart;
    }

    public void setWinStart(String winStart) {
        this.winStart = winStart;
    }

    public String getWinEnd() {
        return winEnd;
    }

    public void setWinEnd(String winEnd) {
        this.winEnd = winEnd;
    }


    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getCurrentDealer() {
        return currentDealer;
    }

    public void setCurrentDealer(String currentDealer) {
        this.currentDealer = currentDealer;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public List<String> getDealers() {
        return dealers;
    }

    public void setDealers(List<String> dealers) {
        this.dealers = dealers;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getLeadNumber() {
        return leadNumber;
    }

    public void setLeadNumber(String leadNumber) {
        this.leadNumber = leadNumber;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Integer> getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(List<Integer> leadStatus) {
        this.leadStatus = leadStatus;
    }

    public String getModifyStartTime() {
        return modifyStartTime;
    }

    public void setModifyStartTime(String modifyStartTime) {
        this.modifyStartTime = modifyStartTime;
    }

    public String getModifyEndTime() {
        return modifyEndTime;
    }

    public void setModifyEndTime(String modifyEndTime) {
        this.modifyEndTime = modifyEndTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSendStart() {
        return sendStart;
    }

    public void setSendStart(String sendStart) {
        this.sendStart = sendStart;
    }

    public String getSendEnd() {
        return sendEnd;
    }

    public void setSendEnd(String sendEnd) {
        this.sendEnd = sendEnd;
    }

    public List<String> getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(List<String> seriesName) {
        this.seriesName = seriesName;
    }

    public List<String> getLevel() {
        return level;
    }

    public void setLevel(List<String> level) {
        this.level = level;
    }

    public List<String> getActCode() {
        return actCode;
    }

    public void setActCode(List<String> actCode) {
        this.actCode = actCode;
    }
}
