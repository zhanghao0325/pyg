package com.pinyougou.sellergoods.service;

import cn.itcast.core.pojo.good.Goods;
import entity.PageResult;
import vo.GoodsVo;

public interface GoodsService {
    void add(GoodsVo goodsVo);

    PageResult search(Integer page, Integer rows, Goods goods);

    GoodsVo findOne(long id);

    void update(GoodsVo vo);

    void delete(long[] ids);

    void updateStatus(long[] ids, String status);
}
