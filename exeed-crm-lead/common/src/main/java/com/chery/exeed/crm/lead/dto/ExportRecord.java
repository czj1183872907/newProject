package com.chery.exeed.crm.lead.dto;


import lombok.Data;

import java.util.Date;

/**
 * 下载报表记录表
 */
@Data
public class ExportRecord {

    private Integer id;
    //用户id
    private Long userId;

    private String userName;//用户名

    private Integer status;//状态 1：为可下载  2：不可下载

    private String module;//模块名称

    private  String fileUrl;//文件地址

    private String fileName;//文件名称

    private Integer downloadNum;//下载次数

    private Date createTime;//创建时间

    private Date updateTime;//更新时间

    private Long createdBy;

    private String reportType;//报表类型


}
