package com.pinyougou.sellergoods.service;



import cn.itcast.core.pojo.template.TypeTemplate;
import entity.PageResult;
import vo.SpecificationVo;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService {

     PageResult search(Integer page, Integer rows, TypeTemplate typeTemplate);

    void add(TypeTemplate typeTemplate);

    TypeTemplate findOne(long id);

    void update(TypeTemplate typeTemplate);

    void delete(long[] ids);

    List<Map> findTemplateList();


    List<Map> findBySpecList(long id);
}
