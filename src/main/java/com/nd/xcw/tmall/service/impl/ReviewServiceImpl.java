package com.nd.xcw.tmall.service.impl;
  
import java.util.List;

import com.nd.xcw.tmall.mapper.ReviewMapper;
import com.nd.xcw.tmall.pojo.Review;
import com.nd.xcw.tmall.pojo.ReviewExample;
import com.nd.xcw.tmall.pojo.User;
import com.nd.xcw.tmall.service.ReviewService;
import com.nd.xcw.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
  
@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    ReviewMapper reviewMapper;
    @Autowired
    UserService userService;
  
    @Override
    public void add(Review c) {
        reviewMapper.insert(c);
    }
  
    @Override
    public void delete(int id) {
        reviewMapper.deleteByPrimaryKey(id);
    }
  
    @Override
    public void update(Review c) {
        reviewMapper.updateByPrimaryKeySelective(c);
    }
  
    @Override
    public Review get(int id) {
        return reviewMapper.selectByPrimaryKey(id);
    }
  
    public List<Review> list(int pid){
        ReviewExample example =new ReviewExample();
        example.createCriteria().andPidEqualTo(pid);
        example.setOrderByClause("id desc");
  
        List<Review> result =reviewMapper.selectByExample(example);
        setUser(result);
        return result;
    }
  
    public void setUser(List<Review> reviews){
        for (Review review : reviews) {
            setUser(review);
        }
    }
  
    private void setUser(Review review) {
        int uid = review.getUid();
        User user =userService.get(uid);
        review.setUser(user);
    }
  
    @Override
    public int getCount(int pid) {
        return list(pid).size();
    }
}