package com.nd.xcw.tmall.mapper;

import com.nd.xcw.tmall.pojo.Property;
import com.nd.xcw.tmall.pojo.PropertyExample;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PropertyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Property record);

    int insertSelective(Property record);

    List<Property> selectByExample(PropertyExample example);

    Property selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Property record);

    int updateByPrimaryKey(Property record);
}