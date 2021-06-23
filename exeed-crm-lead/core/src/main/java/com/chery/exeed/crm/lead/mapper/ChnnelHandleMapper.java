package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.Chnnel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChnnelHandleMapper {
    List<Chnnel> selectChannelByName(String name);
}