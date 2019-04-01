package com.pinyougou.sellergoods.service.impl;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.sellergoods.service.ItemCatService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;

import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    ItemCatDao itemCatDao;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public List<ItemCat> findAll() {
        return itemCatDao.selectByExample(null);

    }

    @Override
    public PageResult search(Integer page, Integer rows, ItemCat itemCat) {

        PageHelper.startPage(page, rows);
        ItemCatQuery itemCatQuery = new ItemCatQuery();

        ItemCatQuery.Criteria criteria = itemCatQuery.createCriteria();

        if (null != itemCat.getStatus() && !"".equals(itemCat.getStatus())){

            criteria.andStatusEqualTo(Long.valueOf(itemCat.getStatus()));
        }

        Page<ItemCat> itemCats = (Page<ItemCat>) itemCatDao.selectByExample(null);

        return new PageResult(itemCats.getTotal(), itemCats.getResult());
    }

    @Override
    public List<ItemCat> findByParentId(long parentId) {
        List<ItemCat> itemCats1 = itemCatDao.selectByExample(null);
        for (ItemCat cat : itemCats1) {
            redisTemplate.boundHashOps("itemCat").put(cat.getName(),cat.getTypeId());

        }
        ItemCatQuery itemCatQuery = new ItemCatQuery();
        itemCatQuery.createCriteria().andParentIdEqualTo(parentId);

        return itemCatDao.selectByExample(itemCatQuery);
    }

    @Override
    public void save(ItemCat itemCat) {
        Long parentId = itemCat.getParentId();

        if (parentId == 0){
            itemCat.setStatus("0");
        }

        itemCatDao.insertSelective(itemCat);
    }

    @Override
    public ItemCat findOne(long id) {

        return itemCatDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(ItemCat itemCat) {
        itemCatDao.updateByPrimaryKeySelective(itemCat);
    }

    @Override
    public void delete(long[] ids) {
        ItemCatQuery itemCatQuery = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = itemCatQuery.createCriteria();
        ItemCatQuery itemCatQuery1 = new ItemCatQuery();

        for (long id : ids) {
            ItemCat itemCat = itemCatDao.selectByPrimaryKey(id);
            Long parentId = itemCat.getId();
            criteria.andParentIdEqualTo(parentId);
            itemCatDao.deleteByPrimaryKey(id);
            List<ItemCat> itemCats = itemCatDao.selectByExample(itemCatQuery);
            itemCatDao.deleteByExample(itemCatQuery);
            for (ItemCat cat : itemCats) {
                Long id1 = cat.getId();
//                criteria.andParentIdEqualTo(id1);
                itemCatQuery1.createCriteria().andParentIdEqualTo(id1);
                List<ItemCat> itemCats1 = itemCatDao.selectByExample(itemCatQuery1);
                if (null != itemCats1) {
                    itemCatDao.deleteByExample(itemCatQuery1);
                }
            }
        }

    }


}
