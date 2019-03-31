package cn.itcast.core.controller;

import cn.itcast.common.utils.IdWorker;
import cn.itcast.core.pojo.user.User;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.UserService;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;

    @RequestMapping("/findAll")
    public List<User> findAll(){

        List<User> list = userService.findAll();

        return list;

    }
    @RequestMapping("/search")
    public PageResult seach(Integer page, Integer rows, @RequestBody(required = false) User user){
      return userService.search(page,rows,user);



    }

}
