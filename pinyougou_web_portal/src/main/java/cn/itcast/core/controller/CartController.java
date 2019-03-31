package cn.itcast.core.controller;

import cn.itcast.core.pojo.order.OrderItem;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.sellergoods.service.CartService;
import com.pinyougou.sellergoods.service.ItemService;
import entity.Cart;
import entity.Result;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("cart")
public class CartController {
    @Reference
    private CartService cartService;
    @Reference
    private ItemService itemService;


    @RequestMapping("addGoodsToCartList")
    @CrossOrigin(origins = "http://localhost:9103", allowCredentials = "true")
    public Result addGoodsToCartList(long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取名字
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            //没登陆

            //1.先查询购物车
            Cookie[] cookies = request.getCookies();
            //判断
            List<Cart> cartList = null;
            if (null != cookies && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if ("CART".equals(cookie.getName())) {
                        String cookieValue = URLDecoder.decode(cookie.getValue(), "UTF-8");//url cookie.getValue();
                        cartList = JSON.parseArray(cookieValue, Cart.class);
                    }
                }
            }
            //如果没有购物车创建一个
            if (cartList == null) {
                cartList = new ArrayList<>();
            }
            //追加商品
            //创建新车
            //查询商家id
            Cart newCart = new Cart();
            newCart.setSellerId(cartService.findId(itemId));
            //创建商品详情
            OrderItem newOrderItem = new OrderItem();
            newOrderItem.setItemId(itemId);
            newOrderItem.setNum(num);
            //创建集合
            ArrayList<OrderItem> list = new ArrayList<>();
            list.add(newOrderItem);
            //添加到新车中
            newCart.setOrderItemList(list);
            int newIndex = cartList.indexOf(newCart); //如果 是-1 不存在  大于等于0 存在  并且知道位置
            if (newIndex >= 0) {
                //1.存在
                //判断商品是否再自购物车中存在
                Cart oldCart = cartList.get(newIndex);
                List<OrderItem> oldCartOrderItemList = oldCart.getOrderItemList();
                int i = oldCartOrderItemList.indexOf(newOrderItem);
                if (i != -1) {
                    //存在此商品
                    OrderItem orderItem = oldCartOrderItemList.get(i);
                    orderItem.setNum(orderItem.getNum() + num);
                } else {
                    //不存在  新增商品
                    oldCartOrderItemList.add(newOrderItem);
                }

            } else {
                //2不存在
                //新增
                //把新车舔到老车中
                cartList.add(newCart);

            }
            //判断是否匿名
            if (!"anonymousUser".equals(name)) {
                //登陆了
                cartService.addCartListToRedis(cartList, name);

                Cookie cookie = new Cookie("CART", null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            } else {
                //一.判断商品的商家是否再购物车中存在

                String s = JSON.toJSONString(cartList);
                String trim = s.trim();

                String encode = URLEncoder.encode(trim, "utf-8");
                Cookie cookie = new Cookie("CART", encode);
                cookie.setMaxAge(60 * 60 * 3 * 24);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");

        }

    }

    @RequestMapping("findCartList")
    public List<Cart> findCartList(HttpServletRequest request, HttpServletResponse response) {
        //判断是购物车是否为空;
        //查询cookie
        //提升作用域
        //判断是否登陆了
        List<Cart> cartList = null;
        Cookie[] cookies = request.getCookies();
        if (null != cookies && cookies.length > 0) {

            for (Cookie cookie : cookies) {

                //判断是否是购物车
                if ("CART".equals(cookie.getName())) {
                    String cookieValue = null;
                    try {
                        cookieValue = URLDecoder.decode(cookie.getValue(), "UTF-8");
                        cartList = JSON.parseArray(cookieValue, Cart.class);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        //判断购物车是否为空

            if (!"anonymousUser".equals(name)) {
                if (null != cartList && cartList.size() > 0) {
                cartService.addCartListToRedis(cartList, name);
                Cookie cookie = new Cookie("CART", null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            //查询cookie中数据查询信息
                cartList = cartService.findCartListByRedis(name);

        }
        if (null != cartList && cartList.size() > 0) {
            cartList = cartService.findCartList(cartList);

        }
        return cartList;

    }

}
