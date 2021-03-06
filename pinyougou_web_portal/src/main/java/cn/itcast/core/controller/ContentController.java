package cn.itcast.core.controller;

import cn.itcast.core.pojo.ad.Content;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.ContentService;
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

}
