package com.chery.exeed.crm.lead.model;

import java.util.Date;

public class Dealer {
    private Integer id;

    private String code;

    private Integer dealerType;

    private String dealerName;

    private String contact;

    private String phone;

    private String city;

    private String cityName;

    private String address;

    private String coordinatesLong;

    private String coordinatesLateral;

    private Integer createBy;

    private Date createDate;

    private Integer chngBy;

    private Date chngDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Integer getDealerType() {
        return dealerType;
    }

    public void setDealerType(Integer dealerType) {
        this.dealerType = dealerType;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName == null ? null : dealerName.trim();
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getCoordinatesLong() {
        return coordinatesLong;
    }

    public void setCoordinatesLong(String coordinatesLong) {
        this.coordinatesLong = coordinatesLong == null ? null : coordinatesLong.trim();
    }

    public String getCoordinatesLateral() {
        return coordinatesLateral;
    }

    public void setCoordinatesLateral(String coordinatesLateral) {
        this.coordinatesLateral = coordinatesLateral == null ? null : coordinatesLateral.trim();
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getChngBy() {
        return chngBy;
    }

    public void setChngBy(Integer chngBy) {
        this.chngBy = chngBy;
    }

    public Date getChngDate() {
        return chngDate;
    }

    public void setChngDate(Date chngDate) {
        this.chngDate = chngDate;
    }
}