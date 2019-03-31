package cn.itcast.core.controller;

import cn.itcast.common.utils.PhoneFormatCheckUtils;
import cn.itcast.core.pojo.user.User;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.UserService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;
    @RequestMapping("/sendCode")
    public Result sendCode(String phone) {
        if (PhoneFormatCheckUtils.isPhoneLegal(phone)) {
            try {
                userService.sendCode(phone);
                return new Result(true, "发送成功");
            } catch (Exception e) {
                return new Result(true, "发送失败");
            }
        }else {
            return new  Result(false,"手机不合法");
        }
    }

    @RequestMapping("/add")
    public Result sendCode(@RequestBody User user, String smscode) {

            try {
                userService.add(user,smscode);
                return new Result(true, "注册成功");
            } catch (RuntimeException e) {
                return new Result(false,e.getMessage());
            } catch (Exception e) {
                return new Result(true, "注册失败");
            }

    }
}
