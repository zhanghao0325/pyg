package cn.itcast.core.controller;

import cn.itcast.core.pojo.item.Item;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.ItemsearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("itemsearch")
public class ItemsearchController {
    @Reference
    private ItemsearchService itemsearchService;

    @RequestMapping("search")
    public Map<String,Object> search(@RequestBody Map searchMap){
       return itemsearchService.search(searchMap);
    }
}
