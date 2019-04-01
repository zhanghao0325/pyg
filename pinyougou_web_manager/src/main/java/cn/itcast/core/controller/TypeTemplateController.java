package cn.itcast.core.controller;

import cn.itcast.core.pojo.template.TypeTemplate;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.TypeTemplateService;
import entity.PageResult;
import entity.Result;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
@RestController
@RequestMapping("typeTemplate")
public class TypeTemplateController {
    @Reference
    TypeTemplateService typeTemplateService;
    @RequestMapping("search")
    public PageResult search(Integer page, Integer rows, @RequestBody TypeTemplate typeTemplate){
        return typeTemplateService.search(page,rows,typeTemplate);

    }
    @RequestMapping("add")
    public Result add(@RequestBody TypeTemplate typeTemplate){
        try {
            typeTemplateService.add(typeTemplate);
            return new Result(true ,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true ,"flase");
        }
    }
    @RequestMapping("update")
    public Result update(@RequestBody TypeTemplate typeTemplate){
        try {
            typeTemplateService.update(typeTemplate);
            return new Result(true ,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false ,"flase");
        }
    }
    @RequestMapping("findOne")
    public TypeTemplate findOne(long id){
        return typeTemplateService.findOne(id);
    }
    @RequestMapping("delete")
    public Result delete(long[] ids){
        try {
            typeTemplateService.delete(ids);
            return new Result(true,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"flase");
        }
    }
    @RequestMapping("findTemplateList")
    public List<Map> findTemplateList(){

        return typeTemplateService.findTemplateList();
    }

    /*
    * 模板 审核
    *
    * */
    @RequestMapping("updateStatus")
    public Result updateStatus(long[] ids,String status){

        try {
            typeTemplateService.updateStatus(ids,status);
            return new Result(true,"审核通过");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"驳回");
        }

    }
}
