package cn.itcast.core.controller;


import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.SeckillService;
import entity.PageResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
* 商家后台秒杀商品 查询
* */
@RestController
@RequestMapping("/seckill")
public class SeckillOrderController {



    @Reference
    private SeckillService seckillService;

    @RequestMapping("search")
    public PageResult search(Integer page, Integer rows, @RequestBody SeckillOrder seckillOrder){

        //获取当前登录人
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        seckillOrder.setSellerId(name);

        return seckillService.search(page,rows,seckillOrder);
    }

}
