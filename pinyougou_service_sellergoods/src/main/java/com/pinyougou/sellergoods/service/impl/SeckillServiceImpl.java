package com.pinyougou.sellergoods.service.impl;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.sellergoods.service.SeckillService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private SeckillGoodsDao seckillGoodsDao;

    @Override
    public PageResult search(Integer page, Integer rows, SeckillGoods seckillGoods) {
        PageHelper.startPage(page,rows);
        SeckillGoodsQuery seckillGoodsQuery = new SeckillGoodsQuery();
        if (null!=seckillGoods.getTitle()&&!"".equals(seckillGoods.getTitle())){
            seckillGoodsQuery.createCriteria().andTitleLike("%"+seckillGoods.getTitle()+"%");
        }
        Page page1= (Page) seckillGoodsDao.selectByExample(seckillGoodsQuery);

        return new PageResult(page1.getTotal(),page1.getResult());
    }
}
