package com.pinyougou.sellergoods.service.impl;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.good.GoodsQuery;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.sellergoods.service.ItemPageService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;


import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import vo.GoodsVo;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    GoodsDescDao goodsDescDao;
    @Autowired
    ItemDao itemDao;
    @Autowired
    ItemCatDao itemCatDao;
    @Autowired
    SellerDao sellerDao;
    @Autowired
    BrandDao brandDao;
    @Autowired
    SolrTemplate solrTemplate;

    //添加时间
    @Override
    public void add(GoodsVo goodsVo) {

        //设置状态
        goodsVo.getGoods().setAuditStatus("0");
        goodsDao.insertSelective(goodsVo.getGoods());

        goodsVo.getGoodsDesc().setGoodsId(goodsVo.getGoods().getId());
        goodsDescDao.insertSelective(goodsVo.getGoodsDesc());
        if ("1".equals(goodsVo.getGoods().getIsEnableSpec())) {
            List<Item> itemList = goodsVo.getItemList();
            for (Item item : itemList) {
                //存储标题
                String title = goodsVo.getGoods().getGoodsName();
                //获取规格
                String spec = item.getSpec();
                //转换格式
                Map<String, String> map = JSON.parseObject(spec, Map.class);
                Set<Map.Entry<String, String>> entries = map.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    String value = entry.getValue();
                    title += " " + value;
                }
                item.setTitle(title);
                //存储图片路径
                String itemImages = goodsVo.getGoodsDesc().getItemImages();
                List<Map> list = JSON.parseArray(itemImages, Map.class);
                //判断是否有图片
                if (null != itemImages && itemImages.length() > 0) {
                    String url = (String) list.get(0).get("url");
                    item.setImage(url);
                }
                //设置三级分类Id
                item.setCategoryid(goodsVo.getGoods().getCategory3Id());
                //设置三级分类的名字
                item.setCategory(itemCatDao.selectByPrimaryKey(goodsVo.getGoods().getCategory3Id()).getName());
                //设置时间
                item.setCreateTime(new Date());
                //设置修改时间
                item.setUpdateTime(new Date());
                //设置商品外键
                item.setGoodsId(goodsVo.getGoods().getId());
                //设置商家
                item.setSeller(sellerDao.selectByPrimaryKey(goodsVo.getGoods().getSellerId()).getName());
                item.setSellerId(goodsVo.getGoods().getSellerId());
                item.setBrand(brandDao.selectByPrimaryKey(goodsVo.getGoods().getBrandId()).getName());
                itemDao.insertSelective(item);
            }
        }

    }

    @Override
    public PageResult search(Integer page, Integer rows, Goods goods) {
        PageHelper.startPage(page, rows);
        GoodsQuery goodsQuery = new GoodsQuery();
        GoodsQuery.Criteria criteria = goodsQuery.createCriteria();


        criteria.andAuditStatusEqualTo("0");

        if (null != goods.getGoodsName() && !"".equals(goods.getGoodsName().trim())) {
            criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");

        }
        criteria.andIsDeleteIsNull();
        Page<Goods> page1 = (Page<Goods>) goodsDao.selectByExample(goodsQuery);
        return new PageResult(page1.getTotal(), page1.getResult());

    }

    @Override
    public GoodsVo findOne(long id) {
        GoodsVo goodsVo = new GoodsVo();

        Goods goods = goodsDao.selectByPrimaryKey(id);
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id);
        List<Item> items = itemDao.selectByExample(itemQuery);
        goodsVo.setGoods(goods);
        goodsVo.setGoodsDesc(goodsDesc);
        goodsVo.setItemList(items);
        return goodsVo;
    }

    @Override
    public void update(GoodsVo goodsVo) {
        goodsDao.updateByPrimaryKeySelective(goodsVo.getGoods());
        goodsDescDao.updateByPrimaryKeySelective(goodsVo.getGoodsDesc());
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(goodsVo.getGoods().getId());
        itemDao.deleteByExample(itemQuery);
        if ("1".equals(goodsVo.getGoods().getIsEnableSpec())) {
            List<Item> itemList = goodsVo.getItemList();
            for (Item item : itemList) {
                //存储标题
                String title = goodsVo.getGoods().getGoodsName();
                //获取规格
                String spec = item.getSpec();
                //转换格式
                Map<String, String> map = JSON.parseObject(spec, Map.class);
                Set<Map.Entry<String, String>> entries = map.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    String value = entry.getValue();
                    title += " " + value;
                }
                item.setTitle(title);
                //存储图片路径
                String itemImages = goodsVo.getGoodsDesc().getItemImages();
                List<Map> list = JSON.parseArray(itemImages, Map.class);
                //判断是否有图片
                if (null != itemImages && itemImages.length() > 0) {
                    String url = (String) list.get(0).get("url");
                    item.setImage(url);
                }
                //设置三级分类Id
                item.setCategoryid(goodsVo.getGoods().getCategory3Id());
                //设置三级分类的名字
                item.setCategory(itemCatDao.selectByPrimaryKey(goodsVo.getGoods().getCategory3Id()).getName());
                //设置时间
                item.setCreateTime(new Date());
                //设置修改时间
                item.setUpdateTime(new Date());
                //设置商品外键
                item.setGoodsId(goodsVo.getGoods().getId());
                //设置商家
                item.setSeller(sellerDao.selectByPrimaryKey(goodsVo.getGoods().getSellerId()).getName());
                item.setSellerId(goodsVo.getGoods().getSellerId());
                item.setBrand(brandDao.selectByPrimaryKey(goodsVo.getGoods().getBrandId()).getName());
                itemDao.insertSelective(item);
            }
        }

    }

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination topicPageAndSolrDestination;
    @Autowired
    private Destination queueSolrDeleteDestination;
    @Override
    public void delete(long[] ids) {
        Goods goods = new Goods();
        goods.setIsDelete("1");
        for (long id : ids) {
            goods.setId(id);
            goodsDao.updateByPrimaryKeySelective(goods);
            jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(String.valueOf(id));
                }
            });
        }
    }

    @Override
    public void updateStatus(long[] ids, String status) {
        Goods goods = new Goods();
        goods.setAuditStatus(status);

        for (final long id : ids) {
            goods.setId(id);
            goodsDao.updateByPrimaryKeySelective(goods);
            if ("1".equals(status)) {
                 jmsTemplate.send(topicPageAndSolrDestination, new MessageCreator() {
                     @Override
                     public Message createMessage(Session session) throws JMSException {
                         return session.createTextMessage(String.valueOf(id));
                     }
                 });

            }
        }
    }
}
