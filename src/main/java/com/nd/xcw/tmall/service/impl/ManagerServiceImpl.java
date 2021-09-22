package com.nd.xcw.tmall.service.impl;

import com.nd.xcw.tmall.mapper.ManagerMapper;
import com.nd.xcw.tmall.pojo.Manager;
import com.nd.xcw.tmall.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerServiceImpl implements ManagerService {
    @Autowired
    ManagerMapper managerMapper;

    @Override
    public Manager get(String username, String password) {
        Manager manager = new Manager();
        manager.setUsername(username);
        manager.setPassword(password);
        return managerMapper.get(manager);
    }
}
