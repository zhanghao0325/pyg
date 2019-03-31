package cn.itcast.core.controller;

import cn.itcast.core.pojo.address.Address;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.AddressService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("address")
public class AddressController {
    @Reference
    private AddressService addressService;
    @RequestMapping("findListByLoginUser")
    public List<Address> findListByLoginUser(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return addressService.findListByLoginUser(name);
    }
}
