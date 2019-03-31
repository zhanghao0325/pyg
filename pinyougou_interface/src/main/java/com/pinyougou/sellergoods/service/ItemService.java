package com.pinyougou.sellergoods.service;

import cn.itcast.core.pojo.item.Item;

import java.util.List;

public interface ItemService {
    List<Item> findAll();


    String findId(long itemId);
}
