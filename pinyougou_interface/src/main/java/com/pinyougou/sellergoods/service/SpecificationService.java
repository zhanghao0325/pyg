package com.pinyougou.sellergoods.service;


import entity.PageResult;
import entity.SearchEntity;
import vo.SpecificationVo;

import java.util.List;
import java.util.Map;


public interface SpecificationService {
    PageResult search(Integer page, Integer rows, SearchEntity searchEntity);

    void add(SpecificationVo specificationVo);

    SpecificationVo findOne(long id);

    void update(SpecificationVo specificationVo);

    void delete(long[] ids);

    List<Map> selectOptionList();

}
