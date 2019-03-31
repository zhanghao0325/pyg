package cn.itcast.core.controller;

import cn.itcast.core.pojo.seller.Seller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.SellerService;
import entity.Result;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference
    SellerService sellerService;

    @RequestMapping("/demo")
    public void demo() {
        sellerService.demo();
    }


    @RequestMapping("add")
    public Result add(@RequestBody Seller seller) {
        try {
            sellerService.add(seller);
            return new Result(true, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "false");
        }

    }

    @RequestMapping("showName")
    public Map<String, Object> showName(HttpServletRequest request) {
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        SecurityContext spring_security_context = (SecurityContext) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        String name = spring_security_context.getAuthentication().getName();
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", name);
        map.put("time", new Date());
        return map;
    }
}
