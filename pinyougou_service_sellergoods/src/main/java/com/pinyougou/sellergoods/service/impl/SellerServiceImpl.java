package com.pinyougou.sellergoods.service.impl;

import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.seller.SellerQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.sellergoods.service.SellerService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sound.midi.Soundbank;

@Service
public class SellerServiceImpl implements SellerService {
    @Autowired
    SellerDao sellerDao;

    @Override
    public void add(Seller seller) {
        if (null != seller) {
           seller.setPassword(new BCryptPasswordEncoder().encode(seller.getPassword()));
           seller.setStatus("0");
            sellerDao.insertSelective(seller);
        }

    }

    @Override
    public PageResult search(Integer page, Integer rows, Seller seller) {
        PageHelper.startPage(page,rows);
        SellerQuery sellerQuery = new SellerQuery();
        SellerQuery.Criteria criteria = sellerQuery.createCriteria();
        if (null!=seller.getName()&&!"".equals(seller.getName().trim())){
            criteria.andNameLike("%"+seller.getName()+"%");
        }
        if (null!=seller.getNickName()&&!"".equals(seller.getNickName().trim())){
            criteria.andNameLike("%"+seller.getNickName()+"%");
        }
        Page<Seller> sellers= (Page<Seller>) sellerDao.selectByExample(sellerQuery);
        return new PageResult(sellers.getTotal(),sellers.getResult());

    }

    @Override
    public Seller findOne(String id) {
        return sellerDao.selectByPrimaryKey(id);
    }

    @Override
    public void updateStatus(String sellerId, String status) {
        Seller seller = new Seller();
        seller.setSellerId(sellerId);
        seller.setStatus(status);
        sellerDao.updateByPrimaryKeySelective(seller);
    }


    @Override
    public void demo() {
        System.out.println(sellerDao);
    }
}
