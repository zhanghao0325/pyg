package cn.itcast.core.controller;

import cn.itcast.core.pojo.ad.Content;
import cn.itcast.core.pojo.ad.ContentCategory;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.ContentService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {
    @Reference
    private ContentService contentService;
    @RequestMapping("/findByCategoryId")
    public List<Content> findByCategoryId(long categoryId ){
        return  contentService.findByCategoryId(categoryId);
    }
    @RequestMapping("search")
    public PageResult search(Integer page, Integer rows, @RequestBody ContentCategory contentCategory){
        return contentService.search(page,rows,contentCategory);

    }
    @RequestMapping("add")
    public Result add(@RequestBody Content content){
        try {
            contentService.add(content);
            return new Result(true,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"false");
        }
    }
    @RequestMapping("update")
    public Result update(@RequestBody Content content){
        try {
            contentService.update(content);
            return new Result(true,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"false");
        }
    }
    @RequestMapping("dele")
    public Result dele(long[] ids){
        try {
            contentService.dele(ids);
            return new Result(true,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"false");
        }
    }
    @RequestMapping("findOne")
    public Content findOne(long id){
        return contentService.findOne(id);
    }
}
