package com.pinyougou.sellergoods.service;

import cn.itcast.core.pojo.order.Order;
import entity.PageResult;

public interface OrderService  {
    void add(Order order,String name);

    PageResult search(Integer page, Integer rows, Order order);
}
