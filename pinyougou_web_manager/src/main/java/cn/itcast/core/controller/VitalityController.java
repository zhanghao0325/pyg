package cn.itcast.core.controller;

import cn.itcast.core.pojo.user.User;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.UserService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.HuoYue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/vitality")
public class VitalityController {
    @Reference
    private UserService userService;

    @RequestMapping("/findAll")
    public HuoYue findAll() {

        List<User> list = userService.findAll();
        HuoYue huoYue = new HuoYue();
        ArrayList<User> huoList = new ArrayList<>();
        ArrayList<User> buList = new ArrayList<>();
        Date nowTime = null;
        Date beginTime = null;
        int a=0;
        int b=0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (User user1 : list) {
            try {
                nowTime = simpleDateFormat.parse(simpleDateFormat.format(user1.getUpdated()));
                beginTime = simpleDateFormat.parse("2018-01-01 00:00:00");

                boolean flag = belongCalendar(nowTime, beginTime);
                if (flag == true) {
                    huoList.add(user1);
                    a++;

                }
                if (flag==false){
                    buList.add(user1);
                    b++;

                }



            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        huoYue.setHuoList(huoList);
        huoYue.setBuList(buList);
        huoYue.setHuoCount(a);
        huoYue.setBuCount(b);
        return huoYue;


    }

    private boolean belongCalendar(Date nowTime, Date beginTime) {

        if (nowTime.getTime() == beginTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);


        if (date.after(begin)) {
            return true;
        } else {
            return false;
        }
    }
}
