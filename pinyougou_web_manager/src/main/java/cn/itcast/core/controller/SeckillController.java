package cn.itcast.core.controller;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.SeckillService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@SuppressWarnings("all")
@RestController
@RequestMapping("seckill")
public class SeckillController {

    @Reference
    private SeckillService seckillService;
    @RequestMapping("search")
    public PageResult search(Integer page , Integer rows, @RequestBody SeckillGoods seckillGoods){
        //return seckillService.search(page,rows,seckillGoods);
        return null;
    }



    /*
    * 审核
    *
    * */
    @RequestMapping("updateStatus")
    public Result updateStatus(long[] ids,String status){

        try {
            //seckillService.updateStatus(ids,status);
            return new Result(true,"审核通过");

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"驳回");
        }


    }

    /*
    * 删除
    * */
    @RequestMapping("delete")
    public Result delete(long[] ids){

        try {
            seckillService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }
 }
