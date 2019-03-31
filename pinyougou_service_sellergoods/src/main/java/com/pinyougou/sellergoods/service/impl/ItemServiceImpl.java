package com.pinyougou.sellergoods.service.impl;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.item.Item;
import com.pinyougou.sellergoods.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemDao itemDao;
    @Override
    public List<Item> findAll() {
        return itemDao.selectByExample(null);

    }

    @Override
    public String findId(long itemId) {
        return  itemDao.selectByPrimaryKey(itemId).getSellerId();

    }
}
