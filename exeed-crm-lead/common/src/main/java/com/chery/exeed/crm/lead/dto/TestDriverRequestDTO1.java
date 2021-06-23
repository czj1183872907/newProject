package com.chery.exeed.crm.lead.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yibo.liu
 * @date 2019/2/22 16:10
 */
public class TestDriverRequestDTO1 implements Serializable {

    private String exeedType;       //车型
    private String provinceId;      //省份ID
    private String province;        //省份
    private String cityId;          //城市ID
    private String city;            //城市
    private String districtId;      //区ID
    private String district;        //区
    private String detaiAddress;    //详细地址
    private String dealers;         //经销商
    private String username;        //姓名
    private String appellation;     //称谓
    private String mobile;          //手机
    private String smsCode;         //手机验证码
    private String unionId;         //微信unionID
    private String openId;          //微信openID
    private String gender;          //性别
    private String email;           //邮箱
    private String siteCode;        //来源
    private String channel;         //渠道
    private String active;          //活动
    private String timeInterval;    //预约时段
    private String driverDate;      //预约日期
    private String leaveWords;      //留言
    private String scContactId;     //
    private Date    createTime;     //

    private String actCode;         //siteCode(也就是campaignCode)对应的活动码，不存入表中
    private String channelCode;     //siteCode(也就是campaignCode)对应的渠道码，不存入表中

    public String getExeedType() {
        return exeedType;
    }

    public void setExeedType(String exeedType) {
        this.exeedType = exeedType;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetaiAddress() {
        return detaiAddress;
    }

    public void setDetaiAddress(String detaiAddress) {
        this.detaiAddress = detaiAddress;
    }

    public String getDealers() {
        return dealers;
    }

    public void setDealers(String dealers) {
        this.dealers = dealers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAppellation() {
        return appellation;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getDriverDate() {
        return driverDate;
    }

    public void setDriverDate(String driverDate) {
        this.driverDate = driverDate;
    }

    public String getLeaveWords() {
        return leaveWords;
    }

    public void setLeaveWords(String leaveWords) {
        this.leaveWords = leaveWords;
    }

    public String getScContactId() {
        return scContactId;
    }

    public void setScContactId(String scContactId) {
        this.scContactId = scContactId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getActCode() {
        return actCode;
    }

    public void setActCode(String actCode) {
        this.actCode = actCode;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }
}
