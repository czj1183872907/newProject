package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.CustomerAuthInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/4/3 11:36
 * @Description:
 */
@Mapper
public interface CustomerAuthInfoSearchMapper extends CustomerAuthInfoMapper{
    CustomerAuthInfo selectByCustomerId(Integer customerId);
}
