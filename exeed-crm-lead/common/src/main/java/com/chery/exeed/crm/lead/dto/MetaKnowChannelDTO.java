package com.chery.exeed.crm.lead.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2020/12/27 14:13
 * @Description:
 */
@Data
public class MetaKnowChannelDTO implements Serializable {

    private String module;

    private String moduleType;

    private String moduleName;

    private String moduleTypeName;

    private List<MetaKnowChannelDTO> subList;

}