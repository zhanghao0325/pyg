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
    /*
    * 分类 审核
    * */
    @Override
    public void updateStatus(long[] ids, String status) {
        //创建分类
        ItemCat itemCat = new ItemCat();
        //二级分类
        ItemCatQuery itemCatQuery = new ItemCatQuery();
        //三级分类
        ItemCatQuery cat1Query = new ItemCatQuery();

        //遍历参数   选项框数组
        for (long id : ids) {
            //设置id
            itemCat.setId(id);
            //设置状态
            itemCat.setStatus(status);
            //更改
            itemCatDao.updateByPrimaryKeySelective(itemCat);
            //二级分类 parentid 等于 一级分类id   作为查询条件
            itemCatQuery.createCriteria().andParentIdEqualTo(id);
            //查询第二级
            List<ItemCat> itemCats1 = itemCatDao.selectByExample(itemCatQuery);
            //遍历第二级
            for (ItemCat cat1 : itemCats1) {


                //System.out.println(cat1.getParentId()+cat1.getName());

                cat1.setStatus(itemCat.getStatus());
                cat1.setId(cat1.getId());
                itemCatDao.updateByPrimaryKeySelective(cat1);

                cat1Query.createCriteria().andParentIdEqualTo(cat1.getId());

                List<ItemCat> itemCats = itemCatDao.selectByExample(cat1Query);

                for (ItemCat cat2 : itemCats) {

                    System.out.println(cat2.getParentId()+cat2.getName());

                    cat2.setStatus(cat1.getStatus());
                    cat2.setId(cat2.getId());
                    itemCatDao.updateByPrimaryKeySelective(cat2);
                }

            }
        }
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
