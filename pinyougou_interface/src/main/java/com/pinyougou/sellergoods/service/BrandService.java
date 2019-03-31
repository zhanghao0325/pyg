package com.pinyougou.sellergoods.service;

import cn.itcast.core.pojo.good.Brand;
import entity.PageResult;
import entity.SearchEntity;

import java.util.List;
import java.util.Map;

public interface BrandService {
    List<Brand> findAll();


    PageResult findPage(Integer page, Integer rows , Brand brand);

    void save(Brand brand);

    Brand findOne(long id);

    void update(Brand brand);

    void delete(long[] ids);

    List<Map> selectOptionList();

    void updateStatus(long[] ids, String status);
}
