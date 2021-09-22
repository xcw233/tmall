package com.nd.xcw.tmall.mapper;

import com.nd.xcw.tmall.pojo.Manager;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ManagerMapper {
    Manager get(Manager manager);
}
