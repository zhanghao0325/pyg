package cn.itcast.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.PayService;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("pay")
public class PayController {
    @Reference
    private PayService payService;

    @RequestMapping("createNative")
    public Map<String, String> createNative() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return payService.createNative(name);
    }

    @RequestMapping("queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        try {
            int i=0;
            while (true) {
                Map<String, String> map = payService.queryPayStatus(out_trade_no);
                if ("NOTPAY".equals(map.get("trade_state"))) {
                    Thread.sleep(4000);
                    i++;
                    if (i>=60){
                        return new Result(false,"支付超时");
                    }
                } else {
                    return new Result(true, "支付成功");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, "支付失败");
        }


    }

}
