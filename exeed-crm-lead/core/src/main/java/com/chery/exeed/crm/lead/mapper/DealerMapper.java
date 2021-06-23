package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.common.dto.DealerDTO;
import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.lead.model.Dealer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DealerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Dealer record);

    int insertSelective(Dealer record);

    Dealer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Dealer record);

    int updateByPrimaryKey(Dealer record);


//    ResponseData<List<DealerDTO>> getDealerInfo();

}