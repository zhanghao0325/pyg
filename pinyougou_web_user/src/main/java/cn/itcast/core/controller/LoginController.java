package cn.itcast.core.controller;


import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("login")
public class LoginController {
    @RequestMapping("name")
    public Map<String,Object> showName(HttpServletRequest request){
        SecurityContext string_security_context = (SecurityContext) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
       User principal = (User) string_security_context.getAuthentication().getPrincipal();
        String name = principal.getUsername();
//        System.out.println("根据对象拿到的名字:"+name);
        String name1 = string_security_context.getAuthentication().getName();
//        System.out.println("直接拿到的名字:"+name1);
        String name2 = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println("安全工具拿到的名字:"+name2);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("loginName",name);
        //hashMap.put("curTime",new Date());
        return hashMap;
    }
}
