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
        if (null != seckillGoods.getStatus() && !"".equals(seckillGoods.getStatus())){
            seckillGoodsQuery.createCriteria().andStatusEqualTo(seckillGoods.getStatus());
        }
        Page page1= (Page) seckillGoodsDao.selectByExample(seckillGoodsQuery);

        return new PageResult(page1.getTotal(),page1.getResult());
    }
    /*
    * 审核
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
    /*
    * 删除
    * */
    @Override
    public void delete(long[] ids) {

        for (long id : ids) {

            seckillGoodsDao.deleteByPrimaryKey(id);
        }
    }
}
