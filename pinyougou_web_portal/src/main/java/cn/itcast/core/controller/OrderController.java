package cn.itcast.core.controller;

import cn.itcast.core.pojo.order.Order;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.OrderService;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @RequestMapping("add")
    public Result add(@RequestBody Order order){
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            orderService.add(order,name);
            return new Result(true,"提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"提交失败");
        }
    }
}
