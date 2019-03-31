package com.pinyougou.sellergoods.service;

import entity.Cart;

import java.util.List;

public interface CartService {


    List<Cart> findCartList(List<Cart> cartList);

    String findId(long itemId);

    void addCartListToRedis(List<Cart> cartList ,String name);

    List<Cart> findCartListByRedis(String name);
}
