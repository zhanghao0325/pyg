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
import org.opensaml.xml.encryption.P;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private SeckillOrderDao seckillOrderDao;

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;
    /*
    * 商家后台 分页 查询
    * */
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
    * 运营商后台 分页 搜索
    * */
    @Override
    public PageResult search(Integer page, Integer rows, SeckillGoods seckillGoods) {
        PageHelper.startPage(page,rows);

        SeckillGoodsQuery seckillGoodsQuery = new SeckillGoodsQuery();

        SeckillGoodsQuery.Criteria criteria = seckillGoodsQuery.createCriteria();

        if (null != seckillGoods.getTitle() && !"".equals(seckillGoods.getTitle())){

            criteria.andTitleLike("%"+seckillGoods.getTitle()+"%");
        }
        if (null != seckillGoods.getStatus() && !"".equals(seckillGoods.getStatus())){

            criteria.andStatusEqualTo(seckillGoods.getStatus());
        }

        Page<SeckillGoods> page1 = (Page<SeckillGoods>) seckillGoodsDao.selectByExample(seckillGoodsQuery);

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
    /*
    * 运营商审核
    * */
    @Override
    public void updateStatus(long[] ids, String status) {

        SeckillGoods seckillGoods = new SeckillGoods();

        seckillGoods.setStatus(status);

        for (long id : ids) {

            seckillGoods.setId(id);

            seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
        }
    }

}
