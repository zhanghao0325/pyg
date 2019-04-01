package cn.itcast.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.Order_statisticsService;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
    @RequestMapping("order_statistics")
public class Order_statisticsController {
    @Reference
    private Order_statisticsService order_statisticsService;
    @RequestMapping("search")
    public PageResult search(Integer page,Integer rows){
        return order_statisticsService.search(page,rows);

    }

}
