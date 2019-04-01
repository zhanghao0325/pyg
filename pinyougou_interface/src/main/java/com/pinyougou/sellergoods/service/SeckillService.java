package com.pinyougou.sellergoods.service;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import entity.PageResult;

public interface SeckillService {
    PageResult search(Integer page, Integer rows, SeckillGoods seckillGoods);

    void updateStatus(long[] ids, String status);

    void delete(long[] ids);
}