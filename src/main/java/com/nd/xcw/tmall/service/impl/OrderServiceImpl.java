package com.nd.xcw.tmall.service.impl;

import java.util.List;

import com.nd.xcw.tmall.mapper.OrderMapper;
import com.nd.xcw.tmall.pojo.Order;
import com.nd.xcw.tmall.pojo.OrderExample;
import com.nd.xcw.tmall.pojo.OrderItem;
import com.nd.xcw.tmall.service.OrderItemService;
import com.nd.xcw.tmall.service.OrderService;
import com.nd.xcw.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemService orderItemService;

    @Autowired
    UserService userService;

    @Override
    public void add(Order c) {
        orderMapper.insert(c);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackForClassName = "Exception")
    public float add(Order c, List<OrderItem> ois) {
        float total = 0;
        add(c);

        if(false)//演示系统发生异常的情况，查看数据库数据是否发生变化
            throw new RuntimeException();

        for(OrderItem oi:ois)
        {
            oi.setOid(c.getId());
            orderItemService.update(oi);
            total+=oi.getProduct().getPromotePrice()*oi.getNumber();
        }
        return total;
    }

    @Override
    public void delete(int id) {
        orderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Order c) {
        orderMapper.updateByPrimaryKeySelective(c);
    }

    @Override
    public Order get(int id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    public List<Order> list(){
        OrderExample example =new OrderExample();
        example.setOrderByClause("id desc");
        return orderMapper.selectByExample(example);

    }

    @Override
    public List list(int uid, String excludedStatus) {
        OrderExample oe = new OrderExample();
        oe.createCriteria().andUidEqualTo(uid).andStatusNotEqualTo(excludedStatus);
        oe.setOrderByClause("id desc");
        List<Order> os = orderMapper.selectByExample(oe);
        return os;
    }


}