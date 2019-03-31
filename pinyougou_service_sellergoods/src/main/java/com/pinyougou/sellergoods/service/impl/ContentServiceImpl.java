package com.pinyougou.sellergoods.service.impl;

import cn.itcast.core.dao.ad.ContentCategoryDao;
import cn.itcast.core.dao.ad.ContentDao;
import cn.itcast.core.pojo.ad.Content;
import cn.itcast.core.pojo.ad.ContentCategory;
import cn.itcast.core.pojo.ad.ContentQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.sellergoods.service.ContentService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;


import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private ContentDao contentDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Content> findByCategoryId(long categoryId) {
        //先从redis中查询
        List<Content> content = (List<Content>) redisTemplate.boundHashOps("content").get(categoryId);
        //有就直接返回
        if (null == content || content.size() == 0) {

            ContentQuery contentQuery = new ContentQuery();
            //没有从mysql中查询
            //再放一份在redis中
            contentQuery.createCriteria().andCategoryIdEqualTo(categoryId).andStatusEqualTo("1");
            contentQuery.setOrderByClause("sort_order desc");
            content = contentDao.selectByExample(contentQuery);
            //设置时间
            redisTemplate.boundHashOps("content").put(categoryId, content);
            redisTemplate.boundHashOps("content").expire(8, TimeUnit.HOURS);
        }

        return content;
    }

    @Override
    public PageResult search(Integer page, Integer rows, ContentCategory contentCategory) {
        PageHelper.startPage(page, rows);
        Page<Content> page1 = (Page<Content>) contentDao.selectByExample(null);
        return new PageResult(page1.getTotal(), page1.getResult());

    }

    @Override
    public void add(Content content) {
        //先存一份到mysql中
        contentDao.insertSelective(content);
        //在看存的的是什么类型的广告
        List<Content> content1 = (List<Content>) redisTemplate.boundHashOps("content").get(content.getCategoryId());
        if (null != content1 && content1.size() > 0) {

            //然后删除redis缓存
            redisTemplate.boundHashOps("content").delete(content.getCategoryId());
        }


    }

    @Override
    public void update(Content content) {
        //先查改动前的数据
        Content content1 = contentDao.selectByPrimaryKey(content.getId());
        //删除缓存中的改动的数据
        redisTemplate.boundHashOps("content").delete(content.getCategoryId());
        //看看内容项目id是否改动
        if (!content.getCategoryId().equals(content1.getCategoryId())) {
            //改动了 删除之前的缓存
            redisTemplate.boundHashOps("content").delete(content1.getCategoryId());

        }
        //先删除之前的redis缓存
        //再放一份新的数据
        contentDao.updateByPrimaryKeySelective(content);
    }

    @Override
    public void dele(long[] ids) {
        Content content = new Content();
        content.setStatus("0");
        for (long id : ids) {
            content.setId(id);
            contentDao.updateByPrimaryKeySelective(content);
            Long categoryId = contentDao.selectByPrimaryKey(id).getCategoryId();
            List<Content> content1 = (List<Content>) redisTemplate.boundHashOps("content").get(categoryId);
            if (null != content1 && content1.size() > 0) {
                redisTemplate.boundHashOps("content").delete(categoryId);
            }
        }
    }

    @Override
    public Content findOne(long id) {
        return contentDao.selectByPrimaryKey(id);

    }

}
