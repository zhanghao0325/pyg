package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.OrderItem;
import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.sellergoods.service.CartService;
import entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.swing.text.Caret;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Cart> findCartList(List<Cart> cartList) {
        for (Cart cart : cartList) {
            List<OrderItem> orderItemList = cart.getOrderItemList();
            for (OrderItem orderItem : orderItemList) {
                Long itemId = orderItem.getItemId();
                Item item = itemDao.selectByPrimaryKey(itemId);
                orderItem.setPicPath(item.getImage());
                orderItem.setPrice(item.getPrice());
                orderItem.setTitle(item.getTitle());
                orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * orderItem.getNum()));
                cart.setSellerName(item.getSeller());
            }


        }
        return cartList;
    }

    @Override
    public String findId(long itemId) {
        return itemDao.selectByPrimaryKey(itemId).getSellerId();

    }

    @Override
    public void addCartListToRedis(List<Cart> cartList, String name) {
        //根据用户名取出购物车
        List<Cart> oldCartList = (List<Cart>) redisTemplate.boundHashOps("cart").get(name);
        oldCartList = mergeCart(cartList, oldCartList);
        //再把合并得购物车存到缓存中;
        redisTemplate.boundHashOps("cart").put(name, oldCartList);
    }

    @Override
    public List<Cart> findCartListByRedis(String name) {
        //查询购物车
        return  (List<Cart>) redisTemplate.boundHashOps("cart").get(name);

    }

    public List<Cart> mergeCart(List<Cart> cartList, List<Cart> oldCartList) {
        if (null != cartList && cartList.size() > 0) {
            //新车不为空
            if (null != oldCartList && oldCartList.size() > 0) {
                //老车不为空
                for (Cart cart : cartList) {
                    int i = oldCartList.indexOf(cart);
                    if (i != -1) {
                        Cart oldcart = oldCartList.get(i);
                        List<OrderItem> oldorderItemList = oldcart.getOrderItemList();
                        for (OrderItem orderItem : cart.getOrderItemList()) {
                            int i1 = oldorderItemList.indexOf(orderItem);
                            if (i1 != -1) {
                                OrderItem orderItem1 = oldorderItemList.get(i1);
                                orderItem1.setNum(orderItem1.getNum()+orderItem.getNum());
                            }else {
                                oldorderItemList.add(orderItem);
                            }

                        }

                    }else {
                        oldCartList.add(cart);
                    }

                }

            } else {
                return cartList;
            }
        }
        return oldCartList;
    }
}
