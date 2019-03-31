package com.pinyougou.sellergoods.service;

import cn.itcast.core.pojo.ad.ContentCategory;
import entity.PageResult;

import java.util.List;

public interface ContentCategoryService {

    List<ContentCategory> findAll();

    PageResult search(Integer page, Integer rows, ContentCategory contentCategory);

    void add(ContentCategory contentCategory);

    void update(ContentCategory content);

    void dele(long[] ids);

    ContentCategory findOne(long id);
}
