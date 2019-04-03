package com.pinyougou.sellergoods.service.impl;

import cn.itcast.common.utils.ExcelUtil;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.sellergoods.service.ItemCatService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
@SuppressWarnings("all")
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
    public List<ItemCat> seleExecle() {
        return itemCatDao.seleByStatus();
    }

    @Override
    public String ajaxUploadExcel(byte[] bytes) {
        System.out.println("得到数据文件");
        if (null == bytes) {
            try {
                throw new Exception("文件不存在！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        InputStream in = null;
        try {
            in = new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("加载流");
        List<List<Object>> list = null;
        try {
            System.out.println("加载流");
            list = new ExcelUtil().getBankListByExcel(in, "jjj");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //该处可调用service相应方法进行数据保存到数据库中，现只对数据输出
        ItemCat vo = new ItemCat();
        for (int i = 0; i < list.size(); i++) {
            List<Object> lo = list.get(i);
            System.out.println("遍历" + list.get(i));
            ItemCat j = null;

            try {
                //j = studentmapper.selectByPrimaryKey(Long.valueOf());
                j = itemCatDao.selectByPrimaryKey(Long.valueOf(String.valueOf(lo.get(0))));
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                System.out.println("没有新增");
            }
            vo.setId(Long.valueOf(String.valueOf(lo.get(0))));
            vo.setParentId(Long.valueOf(String.valueOf(lo.get(1))));
            vo.setName(String.valueOf(lo.get(2)));
            vo.setTypeId(Long.valueOf(String.valueOf(lo.get(3))));
            vo.setStatus(String.valueOf(lo.get(4)));




            if (j == null) {
                itemCatDao.insertSelective(vo);
            } else {
                itemCatDao.updateByPrimaryKey(vo);
            }
        }
        return "very Good";
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
