package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.Chnnel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChnnelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Chnnel record);

    int insertSelective(Chnnel record);

    Chnnel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Chnnel record);

    int updateByPrimaryKey(Chnnel record);
}