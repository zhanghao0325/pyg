package com.pinyougou.sellergoods.service;

import entity.PageResult;

public interface Order_statisticsService {
    PageResult search(Integer page, Integer rows);
}
