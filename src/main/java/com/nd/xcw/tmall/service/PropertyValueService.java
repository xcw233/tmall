package com.nd.xcw.tmall.service;

import com.nd.xcw.tmall.pojo.Product;
import com.nd.xcw.tmall.pojo.PropertyValue;

import java.util.List;

public interface PropertyValueService {
    void init(Product p);
    void update(PropertyValue pv);

    PropertyValue get(int ptid, int pid);
    List<PropertyValue> list(int pid);
}
