package com.pinyougou.sellergoods.service;

import cn.itcast.core.pojo.user.User;
import entity.PageResult;

import java.util.List;

public interface UserService  {

    void sendCode(String phone);

    void add(User user, String smscode);


    List<User> findAll();

    PageResult search(Integer page, Integer rows, User user);
}
