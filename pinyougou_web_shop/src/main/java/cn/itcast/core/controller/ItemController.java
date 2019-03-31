package cn.itcast.core.controller;

import cn.itcast.core.pojo.item.Item;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.ItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("item")
public class ItemController {
    @Reference
    private ItemService itemService;
    @RequestMapping("findAll")
    public List<Item> findAll(){
        return itemService.findAll();
    }
}
