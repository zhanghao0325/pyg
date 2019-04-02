package com.pinyougou.sellergoods.service;

import cn.itcast.core.pojo.item.ItemCat;
import entity.PageResult;

import java.util.List;

public interface ItemCatService {

    PageResult search(Integer page, Integer rows, ItemCat itemCat);

    List<ItemCat> findByParentId(long parentId);

    void save(ItemCat itemCat);

    ItemCat findOne(long id);

    void update(ItemCat itemCat);


    void delete(long[] ids);

    List<ItemCat> findAll();

    void updateStatus(long[] ids, String status);
}
