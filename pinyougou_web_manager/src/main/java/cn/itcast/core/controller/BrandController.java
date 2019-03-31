package cn.itcast.core.controller;

import cn.itcast.core.pojo.good.Brand;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import entity.SearchEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("brand")
public class BrandController {
    @Reference
    private BrandService brandService;

    @RequestMapping("findAll")
    public List<Brand> findAll() {
        List<Brand> list = brandService.findAll();
        return list;
    }

    @RequestMapping("search")
    public PageResult findPage(Integer page, Integer rows, @RequestBody(required = false) Brand brand) {


        return brandService.findPage(page, rows, brand);


    }

    @RequestMapping("save")
    public Result save(@RequestBody Brand brand) {
        try {
            brandService.save(brand);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            return new Result(false, "保存失败");

        }

    }

    @RequestMapping("update")
    public Result update(@RequestBody Brand brand) {
        try {
            brandService.update(brand);
            return new Result(true, "success");
        } catch (Exception e) {
            return new Result(false, "保存失败");

        }

    }

    @RequestMapping("findOne")
    public Brand findOne(long id) {
        return brandService.findOne(id);
    }

    @RequestMapping("delete")
    public Result delete(@RequestBody long[] deletelist) {

//        System.out.println(deletelist);
        try {
            brandService.delete(deletelist);
            return new Result(true, "success");
        } catch (Exception e) {
            return new Result(false, "删除失败");

        }

    }

    //查询所有品牌
    @RequestMapping("selectOptionList")
    public List<Map> selectOptionList() {

        return brandService.selectOptionList();
    }

    //修改审核状态
    @RequestMapping("updateStatus")
    public  Result  updateStatus(long[] ids,String status){

        try {
            brandService.updateStatus(ids,status);
            return new Result(true,"审核通过");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"驳回");
        }

    }

}
