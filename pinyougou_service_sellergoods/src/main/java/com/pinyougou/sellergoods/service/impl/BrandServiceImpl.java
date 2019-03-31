package com.pinyougou.sellergoods.service.impl;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandDao brandDao;

    @Override
    public List<Brand> findAll() {
        return brandDao.selectByExample(null);
    }

    @Override
    public PageResult findPage(Integer page, Integer rows, Brand brand) {


            PageHelper.startPage(page,rows);
            BrandQuery brandQuery = new BrandQuery();
            BrandQuery.Criteria criteria = brandQuery.createCriteria();
        if (null!=brand){
            if (null != brand.getName() && !"".equals(brand.getName().trim())) {

                criteria.andNameLike("%" + brand.getName().trim() + "%");
            }
            if (null != brand.getFirstChar() && !"".equals(brand.getFirstChar().trim())) {

                criteria.andFirstCharEqualTo(brand.getFirstChar().trim());
            }

        }

            Page<Brand> list = (Page<Brand>) brandDao.selectByExample(brandQuery);

            PageResult pageResult = new PageResult(list.getTotal(), list.getResult());
            return pageResult;


    }

    @Override
    public void save(Brand brand) {
        brandDao.insertSelective(brand);
    }

    @Override
    public Brand findOne(long id) {
        return brandDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(Brand brand) {
        brandDao.updateByPrimaryKeySelective(brand);
    }

    @Override
    public void delete(long[] ids) {
        for (long id : ids) {
            brandDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<Map> selectOptionList() {
        return  brandDao.selectAll();

    }
}
