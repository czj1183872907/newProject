package com.chery.exeed.crm.lead.dto;

import java.io.Serializable;

/**
 * @author yibo.liu
 * @date 2019/4/11 9:54
 */
public class CrmRelationDto implements Serializable {

    private Integer id;

    private String crmCode;          //客户编号

    private String dmsCode;          //客户编号

    private String salesDealerCode;     //售出经销商编号

    private String vin;                 //VIN

    private Integer relation;           //关系

    private Integer type;               //类型

    private Integer isDelete;           //是否删除

    private Integer isDeal;             //是否推送到MQ

    private String subNetworkCode;      //二网经销商编号

    private String categoryName;//车系

    private String modelCode;//车型

    private String productCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCrmCode() {
        return crmCode;
    }

    public void setCrmCode(String crmCode) {
        this.crmCode = crmCode;
    }

    public String getDmsCode() {
        return dmsCode;
    }

    public void setDmsCode(String dmsCode) {
        this.dmsCode = dmsCode;
    }

    public String getSalesDealerCode() {
        return salesDealerCode;
    }

    public void setSalesDealerCode(String salesDealerCode) {
        this.salesDealerCode = salesDealerCode;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getIsDeal() {
        return isDeal;
    }

    public void setIsDeal(Integer isDeal) {
        this.isDeal = isDeal;
    }

    public String getSubNetworkCode() {
        return subNetworkCode;
    }

    public void setSubNetworkCode(String subNetworkCode) {
        this.subNetworkCode = subNetworkCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
