package com.nd.xcw.tmall.service;
  
import java.util.List;

import com.nd.xcw.tmall.pojo.Review;

public interface ReviewService {
      
    void add(Review c);
 
    void delete(int id);
    void update(Review c);
    Review get(int id);
    List list(int pid);
 
    int getCount(int pid);
}