package com.pinyougou.sellergoods.service;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import entity.PageResult;

public interface SeckillService {
    PageResult search(Integer page, Integer rows, SeckillOrder seckillOrder);


    void delete(long[] ids);


}
