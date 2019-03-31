package cn.itcast.core.controller;

import cn.itcast.core.pojo.ad.Content;
import cn.itcast.core.pojo.ad.ContentCategory;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.ContentCategoryService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("contentCategory")
public class ContentCategoryController {
    @Reference
    private ContentCategoryService contentCategoryService;
    @RequestMapping("findAll")
    public List<ContentCategory> findAll(){
        return contentCategoryService.findAll();
    }
    @RequestMapping("search")
    public PageResult search(Integer page, Integer rows, @RequestBody ContentCategory contentCategory){
        return contentCategoryService.search(page,rows,contentCategory);

    }
    @RequestMapping("add")
    public Result add(@RequestBody ContentCategory contentCategory){
        try {
            contentCategoryService.add(contentCategory);
            return new Result(true,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"false");
        }
    }
    @RequestMapping("update")
    public Result update(@RequestBody ContentCategory content){
        try {
            contentCategoryService.update(content);
            return new Result(true,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"false");
        }
    }
    @RequestMapping("dele")
    public Result dele(long[] ids){
        try {
            contentCategoryService.dele(ids);
            return new Result(true,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"false");
        }
    }
    @RequestMapping("findOne")
    public ContentCategory findOne(long id){
        return contentCategoryService.findOne(id);
    }
}
