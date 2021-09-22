package com.nd.xcw.tmall.service;
 
import java.util.List;

import com.nd.xcw.tmall.pojo.Order;
import com.nd.xcw.tmall.pojo.OrderItem;

public interface OrderItemService {
     

    void add(OrderItem c);

    void delete(int id);
    void update(OrderItem c);
    OrderItem get(int id);
    List list();

    void fill(List<Order> os);

    void fill(Order o);

    int getSaleCount(int  pid);

    /**
     * @param uid
     * @return 返回与用户有关的所有订单项
     */
    List<OrderItem> listByUser(int uid);
}