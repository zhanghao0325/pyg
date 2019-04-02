package com.pinyougou.sellergoods.service;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import entity.PageResult;

public interface SeckillService {
    PageResult search(Integer page, Integer rows, SeckillOrder seckillOrder);


    PageResult search(Integer page, Integer rows, SeckillGoods seckillGoods);



    void delete(long[] ids);


    void updateStatus(long[] ids, String status);
}
