package cn.itcast.core.controller;

import cn.itcast.core.pojo.item.ItemCat;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.ItemCatService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SuppressWarnings("all")
@RestController
@RequestMapping("itemCat")
public class ItemCatController {
    @Reference
    ItemCatService itemCatService;

    @RequestMapping("findAll")
    public List<ItemCat> findAll(){
        return itemCatService.findAll();
    }



    @RequestMapping("search")
    public PageResult search(Integer page, Integer rows, @RequestBody ItemCat itemCat) {
        return itemCatService.search(page, rows, itemCat);

    }

    @RequestMapping("findByParentId")
    public List<ItemCat> findByParentId(long parentId) {

        return itemCatService.findByParentId(parentId);
    }

    @RequestMapping("add")
    public Result save(@RequestBody ItemCat itemCat) {
        try {
            itemCatService.save(itemCat);
            return new Result(true, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "false");
        }
    }

    @RequestMapping("findOne")
    public ItemCat findOne(long id) {
        return itemCatService.findOne(id);
    }

    @RequestMapping("update")
    public Result update(@RequestBody ItemCat itemCat) {
        try {
            itemCatService.update(itemCat);
            return new Result(true, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "false");
        }

    }

    @RequestMapping("delete")
    public Result delete(long[] ids) {
        try {
            itemCatService.delete(ids);
            return new Result(true,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"false");
        }
    }

    /*
    * 分类 审核
    * */
    @RequestMapping("updateStatus")
    public Result updateStatus(long[] ids,String status){

        try {
            itemCatService.updateStatus(ids,status);
            return new Result(true,"审核通过");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"驳回");
        }

    }
}
