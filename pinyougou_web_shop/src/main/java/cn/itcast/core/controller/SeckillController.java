package cn.itcast.core.controller;


import cn.itcast.core.pojo.seckill.SeckillGoods;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.SeckillService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
* 秒杀商品查询
* */
@SuppressWarnings("all")
@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Reference
    private SeckillService seckillService;

    /*
    * 分页查询  搜索
    */
    @RequestMapping("search")
    public PageResult search(Integer page, Integer rows, @RequestBody SeckillGoods seckillGoods){

        return seckillService.search(page,rows,seckillGoods);

    }

    /*
    * 秒杀商品审核
    * */
    @RequestMapping("updateStatus")
    public Result updateStatus(long[] ids, String status){

        try {
            seckillService.updateStatus(ids,status);
            return new Result(true,"审核通过");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"驳回");
        }
    }

}
