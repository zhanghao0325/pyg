package com.pinyougou.sellergoods.service.impl;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.pojo.seckill.SeckillOrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.sellergoods.service.SeckillService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private SeckillOrderDao seckillOrderDao;

    @Override
    public PageResult search(Integer page, Integer rows, SeckillOrder seckillOrder) {
        PageHelper.startPage(page,rows);
        SeckillOrderQuery seckillOrderQuery = new SeckillOrderQuery();
       /* if (null!=seckillOrder.getTitle()&&!"".equals(seckillOrder.getTitle())){
            seckillOrderQuery.createCriteria().andTitleLike("%"+seckillOrder.getTitle()+"%");
        }*/
       if (null != seckillOrder.getStatus() && !"".equals(seckillOrder.getStatus())){
            seckillOrderQuery.createCriteria().andStatusEqualTo(seckillOrder.getStatus());
        }

        if (null != seckillOrder.getSellerId() && !"".equals(seckillOrder.getSellerId())){

            seckillOrderQuery.createCriteria().andSellerIdEqualTo(seckillOrder.getSellerId());
        }

        Page page1= (Page) seckillOrderDao.selectByExample(seckillOrderQuery);

        return new PageResult(page1.getTotal(),page1.getResult());
    }
    /*
    * 删除
    * */
    @Override
    public void delete(long[] ids) {

        for (long id : ids) {

            seckillOrderDao.deleteByPrimaryKey(id);
        }
    }

}
