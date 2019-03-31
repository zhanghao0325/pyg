package cn.itcast.core.service;

import cn.itcast.common.utils.IdWorker;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.sellergoods.service.OrderService;
import entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private PayLogDao payLogDao;

    @Override
    public void add(Order order, String name) {

        //地址 手机 联系人 支付类型 都有 不用再存了
        //实付金额 从redis 中查询  再从数据库中查询
        //redis中存有购物车数据  商家id item库存id 数量 num;
        List<Cart> cartlist = (List<Cart>) redisTemplate.boundHashOps("cart").get(name);
        long s = 0;
        ArrayList<Long> list = new ArrayList<>();
        if (null != cartlist && cartlist.size() > 0) {
            for (Cart cart : cartlist) {
                double totol = 0;
                //订单来源
                order.setSourceType("2");
                long id = idWorker.nextId();
                list.add(id);
                // 订单id
                order.setOrderId(id);
                //支付类型，1、在线支付，2、货到付款
                //状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
                order.setStatus("1");
                //订单创建时间
                order.setCreateTime(new Date());
                //订单更新时间
                order.setUpdateTime(new Date());
                //商家id
                order.setSellerId(cart.getSellerId());
                order.setUserId(name);
                for (OrderItem orderItem : cart.getOrderItemList()) {
                    Long itemId = orderItem.getItemId();
                    Item item = itemDao.selectByPrimaryKey(itemId);
                    totol += item.getPrice().doubleValue() * orderItem.getNum();
                    //订单详情id
                    long orderId = idWorker.nextId();
                    orderItem.setId(orderId);
                    //设置商品id
                    orderItem.setGoodsId(item.getGoodsId());
                    //设置订单id
                    orderItem.setOrderId(id);
                    //设置标题
                    orderItem.setTitle(item.getTitle());
                    //设置价格
                    orderItem.setPrice(item.getPrice());
                    //设置小计
                    orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * orderItem.getNum()));
                    //设置图片路径
                    orderItem.setPicPath(item.getImage());
                    //设置商家id
                    orderItem.setSellerId(cart.getSellerId());
                    orderItemDao.insertSelective(orderItem);
                }
                //设置总金额
                order.setPayment(new BigDecimal(totol));
                s += totol * 100;
                orderDao.insertSelective(order);
            }
            PayLog payLog = new PayLog();
            payLog.setOutTradeNo(String.valueOf(idWorker.nextId()));
            payLog.setCreateTime(new Date());
            payLog.setTotalFee(s);
            payLog.setUserId(order.getUserId());
            payLog.setPayType("1");
            payLog.setOrderList(list.toString().replace("[", "").replace("]", ""));
            payLog.setTradeState("0");
            payLogDao.insertSelective(payLog);
            redisTemplate.boundHashOps("payLog").put(name, payLog);
            redisTemplate.boundHashOps("cart").delete(name);

        }
    }
}
