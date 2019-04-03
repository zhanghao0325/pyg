package com.pinyougou.sellergoods.service;


import cn.itcast.core.pojo.specification.Specification;
import entity.PageResult;
import entity.SearchEntity;
import vo.SpecificationVo;

import java.util.List;
import java.util.Map;


public interface SpecificationService {
    PageResult search(Integer page, Integer rows, Specification specification);

    void add(SpecificationVo specificationVo);

    SpecificationVo findOne(long id);

    void update(SpecificationVo specificationVo);

    void delete(long[] ids);

    List<Map> selectOptionList();

    void updateStatus(long[] ids, String status);

    String ajaxUploadExcel(byte[] bytes);

    List<Specification> seleExecle();
}
