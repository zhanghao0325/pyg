package cn.itcast.core.controller;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.SeckillService;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("seckill")
public class SeckillController {
    @Reference
    private SeckillService seckillService;
    @RequestMapping("search")
    public PageResult search(Integer page , Integer rows, @RequestBody SeckillGoods seckillGoods){
        return seckillService.search(page,rows,seckillGoods);
    }
 }
