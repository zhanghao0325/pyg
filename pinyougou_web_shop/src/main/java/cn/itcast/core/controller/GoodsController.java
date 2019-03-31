package cn.itcast.core.controller;

import cn.itcast.core.pojo.good.Goods;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.GoodsService;
import entity.PageResult;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.GoodsVo;

@RestController
@RequestMapping("goods")
public class GoodsController {
    @Reference
    private GoodsService goodsService;

    @RequestMapping("add")
    public Result add(@RequestBody GoodsVo goodsVo) {
        try {
            //商家id
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            goodsVo.getGoods().setSellerId(name);
            goodsService.add(goodsVo);
            return new Result(true, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "false");
        }

    }

    @RequestMapping("search")
    public PageResult search(Integer page, Integer rows, @RequestBody Goods goods) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(name);
        return  goodsService.search(page, rows, goods);
    }
    @RequestMapping("findOne")
    public GoodsVo findOne(long id){
       return goodsService.findOne(id);
    }
    @RequestMapping("update")
    public Result update(@RequestBody GoodsVo vo){
        try {
            goodsService.update(vo);
            return  new Result(true,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,"false");
        }
    }
    @RequestMapping("delete")
    public Result delete(long[] ids){
        try {
            goodsService.delete(ids);
            return new Result(true,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"false");
        }
    }
}
