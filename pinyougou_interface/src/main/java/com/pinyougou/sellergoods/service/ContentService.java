package com.pinyougou.sellergoods.service;

import cn.itcast.core.pojo.ad.Content;
import cn.itcast.core.pojo.ad.ContentCategory;
import entity.PageResult;


import java.util.List;

public interface ContentService {

    List<Content> findByCategoryId(long categoryId);


    PageResult search(Integer page, Integer rows, ContentCategory contentCategory);

    void add(Content content);

    void update(Content content);

    void dele(long[] ids);

    Content findOne(long id);
}
