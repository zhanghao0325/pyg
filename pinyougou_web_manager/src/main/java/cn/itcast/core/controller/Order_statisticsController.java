package cn.itcast.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.Order_statisticsService;
import entity.PageResult;
import entity.Result;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
@SuppressWarnings("all")
@RestController
    @RequestMapping("order_statistics")
public class Order_statisticsController {
    @Reference
    private Order_statisticsService order_statisticsService;
    @RequestMapping("search")
    public PageResult search(Integer page,Integer rows){
        return order_statisticsService.search(page,rows);

    }


}
