package com.nd.xcw.tmall.mapper;

import com.nd.xcw.tmall.pojo.PropertyValue;
import com.nd.xcw.tmall.pojo.PropertyValueExample;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PropertyValueMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PropertyValue record);

    int insertSelective(PropertyValue record);

    List<PropertyValue> selectByExample(PropertyValueExample example);

    PropertyValue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PropertyValue record);

    int updateByPrimaryKey(PropertyValue record);
}