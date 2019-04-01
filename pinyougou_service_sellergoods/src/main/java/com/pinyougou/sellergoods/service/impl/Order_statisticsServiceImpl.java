package com.pinyougou.sellergoods.service.impl;

import cn.itcast.common.utils.IdWorker;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.pojo.user.User;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.pinyougou.sellergoods.service.Order_statisticsService;
import entity.Oder_statistics;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class Order_statisticsServiceImpl implements Order_statisticsService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private UserDao userDao;

    @Override
    public PageResult search(Integer page, Integer rows) {
//        SELECT UID as 用户 ,COUNT(ORDER_SN)as 订单总数,SUM(TOTAL)as 合计总金额 FROM 订单表 group by uid

        List<User> users = userDao.selectByExample(null);
        ArrayList<Oder_statistics> oder_statisticsList = new ArrayList<>();
        for (User user : users) {
            Oder_statistics oder_statistics = new Oder_statistics();
            oder_statistics.setId(new IdWorker().nextId());
            String username = user.getUsername();
            oder_statistics.setUsername(username);
            OrderQuery orderQuery = new OrderQuery();
            orderQuery.createCriteria().andUserIdEqualTo(username);
            List<Order> orders = orderDao.selectByExample(orderQuery);
            oder_statistics.setOrderTotal(orders.size());
            double payment = oder_statistics.getPayment();
            if (null != orders && orders.size() > 0) {
                for (Order order : orders) {
                    if (null!=order.getPayment()){
                    payment+=order.getPayment().doubleValue();
                    }
                }
            }
            oder_statistics.setPayment(payment);

            oder_statisticsList.add(oder_statistics);
        }
        return new PageResult(Long.parseLong(String.valueOf(oder_statisticsList.size())), oder_statisticsList);

    }
}
