package com.chery.exeed.crm.lead.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/6/19 11:56
 * @Description:
 */
public class CustAdvancedQueryDTO implements Serializable {
    private Integer pageNo;
    private Integer pageSize;

    private String createBy; // 创建人
    private List<String> channels; // 多个渠道
    private String createStart; // 档案创建开始时间
    private String createEnd; // 档案创建截至时间
    private String phone; // 手机号
    private String custName; // 客户姓名
    private Integer province; // 客户所在省
    private Integer city; // 客户所在市
    private Integer subcity; // 客户所在区/县
    private Integer customerStatus;//1:潜客  2：保客

    public Integer getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(Integer customerStatus) {
        this.customerStatus = customerStatus;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public String getCreateStart() {
        return createStart;
    }

    public void setCreateStart(String createStart) {
        this.createStart = createStart;
    }

    public String getCreateEnd() {
        return createEnd;
    }

    public void setCreateEnd(String createEnd) {
        this.createEnd = createEnd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getSubcity() {
        return subcity;
    }

    public void setSubcity(Integer subcity) {
        this.subcity = subcity;
    }
}
