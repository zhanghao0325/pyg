package com.pinyougou.sellergoods.service.impl;

import cn.itcast.core.dao.ad.ContentCategoryDao;
import cn.itcast.core.pojo.ad.ContentCategory;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.sellergoods.service.ContentCategoryService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private ContentCategoryDao contentCategoryDao;

    @Override
    public List<ContentCategory> findAll() {
        return contentCategoryDao.selectByExample(null);

    }

    @Override
    public PageResult search(Integer page, Integer rows, ContentCategory contentCategory) {
        PageHelper.startPage(page, rows);
        Page<ContentCategory> page1 = (Page<ContentCategory>) contentCategoryDao.selectByExample(null);
        return new PageResult(page1.getTotal(), page1.getResult());

    }

    @Override
    public void add(ContentCategory contentCategory) {
        contentCategoryDao.insertSelective(contentCategory);
    }

    @Override
    public void update(ContentCategory content) {
        contentCategoryDao.updateByPrimaryKeySelective(content);
    }

    @Override
    public void dele(long[] ids) {
        for (long id : ids) {
            contentCategoryDao.deleteByPrimaryKey(id);

        }
    }

    @Override
    public ContentCategory findOne(long id) {
        return contentCategoryDao.selectByPrimaryKey(id);
    }
}
