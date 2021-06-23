package com.chery.exeed.crm.lead.dto;

import java.io.Serializable;

public class DccLeadDTO implements Serializable {

    private static final long serialVersionUID = 6950713121636785292L;

    private Integer leadsId;//接口本地ID
    private String id;//dcc服务商的线索 ID
    private Integer cityId;//城市 ID
    private String cityName;//城市名称
    private Integer provinceId;//省份 ID
    private String provinceName;//省份名称
    private Integer countyId;//区县ID
    private String countyName;//区县名称
    private String dealerId;//经销商 ID
    private String dealerName;//经销商名称
    private String createTime;//线索创建时间 yyyy-MM-dd HH:mm:ss
    private String name;//客户姓名
    private String gender;//客户性别
    private String mail;//客户邮箱
    private String phone;//客户电话
    private Integer type;//线索类型  1. 询价 2. 试驾 3.新车 4. 活动 5.置换
    private String modelId;//车型 ID
    private String modelName;//车型名称
    private String seriesId;//车系Id
    private String seriesName;//车系名称
    private String remark;//备注
    private String salesName;//销售顾问
    private String salesPhone;//销售顾问电话
    private String beginTime;//通话开始时间 yyyy-MM-dd HH:mm:ss
    private String endTime;//通话结束时间 yyyy-MM-dd HH:mm:ss
    private Integer callResult;//通话结果(0:未接通;1:成功接通)
    private String content;//呼叫失败原因
    private String calledNo;//被叫号码
    private String userKey;//用户按键
    private String poc400Phone;//400 电话号码
    private Integer talkTime;//通话时长(秒)
    private Integer waitTime;//等待时长
    private Integer leadType;//线索类别 1:订单线索 2:话单线索
    private String channel;//渠道 1易车，2汽车之家，3太平洋
    private Integer isPublic;//是否公共线索，0：私有线索； 1：公共线索
    private String reDealerId;//处理订单经销商编号（推送给此商家）
    private String handleTime;//处理时间
    private String productType;//产品类型
    private String fkDealerId;//对应厂商经销商id
    private String fkSeriesId;//对应厂商车系id
    private String fkSpecId;//对应厂商车型id
    private Integer newSiteId;//线索来源
    private String newSiteDesc;//线索来源描述
    private Integer status;//提供商的线索处理状态 0:未处理 | 1:已处理 |
    private String campaignId;//活动id

    public Integer getLeadsId() {
        return leadsId;
    }

    public void setLeadsId(Integer leadsId) {
        this.leadsId = leadsId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getSalesPhone() {
        return salesPhone;
    }

    public void setSalesPhone(String salesPhone) {
        this.salesPhone = salesPhone;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getCallResult() {
        return callResult;
    }

    public void setCallResult(Integer callResult) {
        this.callResult = callResult;
    }

    public String getCalledNo() {
        return calledNo;
    }

    public void setCalledNo(String calledNo) {
        this.calledNo = calledNo;
    }

    public String getPoc400Phone() {
        return poc400Phone;
    }

    public void setPoc400Phone(String poc400Phone) {
        this.poc400Phone = poc400Phone;
    }

    public Integer getTalkTime() {
        return talkTime;
    }

    public void setTalkTime(Integer talkTime) {
        this.talkTime = talkTime;
    }

    public Integer getLeadType() {
        return leadType;
    }

    public void setLeadType(Integer leadType) {
        this.leadType = leadType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public Integer getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Integer waitTime) {
        this.waitTime = waitTime;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public String getReDealerId() {
        return reDealerId;
    }

    public void setReDealerId(String reDealerId) {
        this.reDealerId = reDealerId;
    }

    public String getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getFkDealerId() {
        return fkDealerId;
    }

    public void setFkDealerId(String fkDealerId) {
        this.fkDealerId = fkDealerId;
    }

    public String getFkSeriesId() {
        return fkSeriesId;
    }

    public void setFkSeriesId(String fkSeriesId) {
        this.fkSeriesId = fkSeriesId;
    }

    public String getFkSpecId() {
        return fkSpecId;
    }

    public void setFkSpecId(String fkSpecId) {
        this.fkSpecId = fkSpecId;
    }

    public Integer getNewSiteId() {
        return newSiteId;
    }

    public void setNewSiteId(Integer newSiteId) {
        this.newSiteId = newSiteId;
    }

    public String getNewSiteDesc() {
        return newSiteDesc;
    }

    public void setNewSiteDesc(String newSiteDesc) {
        this.newSiteDesc = newSiteDesc;
    }

    public Integer getCountyId() {
        return countyId;
    }

    public void setCountyId(Integer countyId) {
        this.countyId = countyId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }
}
