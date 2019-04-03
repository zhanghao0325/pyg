package cn.itcast.user.service.impl;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsQuery;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.sellergoods.service.UserService;

import entity.PageResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserDao userDao;
    @Override
    public void sendCode(final String phone) {
        String random = RandomStringUtils.randomNumeric(6);
        redisTemplate.boundValueOps(phone).set(random);
        jmsTemplate.send("sms", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage map = session.createMapMessage();
                map.setString("phone",phone);
                map.setString("signName","独乐");
                map.setString("templateCode","SMS_160861751");
                map.setString("templateParam","{\"code\":\""+random+"\"}");
                return map;
            }
        });
    }

    @Override
    public void add(User user, String smscode) {
        String code = (String) redisTemplate.boundValueOps(user.getPhone()).get();
        if (null==code){
            throw new RuntimeException("验证码已过期");
        }else if (smscode==code){
            user.setCreated(new Date());
            user.setUpdated(new Date());
            userDao.insertSelective(user);
        }else {
            throw new RuntimeException("验证码不正确");
        }

    }

    @Override
    public List<User>findAll() {

      return  userDao.selectByExample(null);

    }

    @Override
    public PageResult search(Integer page, Integer rows, User user) {
        PageHelper.startPage(page,rows);
        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        if (null!=user&&!"".equals(user.getUsername())){
            if (null!=user.getUsername()&& !"".equals(user.getUsername().trim())){
                criteria.andNameLike("%"+user.getUsername()+"%");
            }

        }
         if (null!=user&&!"".equals(user.getPhone())){
             if (null!=user.getPhone() && !"".equals(user.getPhone().trim())){
                 criteria.andPhoneLike("%"+user.getPhone()+"%");
             }
         }
//        User user1 = userDao.selectByPrimaryKey((long) 10);

        Page<User> users = (Page<User>) userDao.selectByExample(userQuery);
        PageResult pageResult = new PageResult(users.getTotal(), users.getResult());

        return  pageResult;
    }

    @Override
    public PageResult find(Integer page, Integer rows, User user) {
        PageHelper.startPage(page,rows);
        System.out.println("page:"+page+"rows:"+rows);
        Page<User> users= (Page<User>) userDao.selectByExample(null);
        System.out.println(users.getTotal());
        System.out.println(users.getResult());
        return new PageResult(users.getTotal(),users.getResult());

    }
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private GoodsDao goodsDao;

    @Override
    public List<Goods> seleExecle(Long id) {
        OrderQuery query=new OrderQuery();
        OrderQuery.Criteria criteria = query.createCriteria();
        criteria.andUserIdEqualTo(String.valueOf(id));
        ArrayList<Goods> list = new ArrayList<>();
        List<Order> order1 = (List<Order>) orderDao.selectByExample(query);
        for (Order order2 : order1) {
            Long orderId = order2.getOrderId();
            OrderItemQuery orderItemQuery = new OrderItemQuery();
            OrderItemQuery.Criteria criteria1 = orderItemQuery.createCriteria();
            criteria1.andOrderIdEqualTo(orderId);
            List<OrderItem> orderItem = (List<OrderItem>) orderItemDao.selectByExample(orderItemQuery);
            for (OrderItem item : orderItem) {
                Long goodsId = item.getGoodsId();
                GoodsQuery goodsQuery = new GoodsQuery();
                GoodsQuery.Criteria criteria2 = goodsQuery.createCriteria();
                criteria2.andIdEqualTo(goodsId);
                List<Goods> goods = (List<Goods>) goodsDao.selectByExample(goodsQuery);
                for (Goods good : goods) {
                    list.add(good);
                }
            }

        }

        return list;
    }
}
