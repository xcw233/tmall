package com.nd.xcw.tmall.service;
 
import java.util.List;

import com.nd.xcw.tmall.pojo.User;

public interface UserService {
    void add(User c);
    void delete(int id);
    void update(User c);
    User get(int id);
    List list();
    boolean isExist(String name);

    User get(String name, String password);
}