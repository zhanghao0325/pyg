package cn.itcast.user.service.impl;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.sellergoods.service.UserService;

import entity.PageResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserDao userDao;
    @Override
    public void sendCode(final String phone) {
        String random = RandomStringUtils.randomNumeric(6);
        redisTemplate.boundValueOps(phone).set(random);
        jmsTemplate.send("sms", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage map = session.createMapMessage();
                map.setString("phone",phone);
                map.setString("signName","独乐");
                map.setString("templateCode","SMS_160861751");
                map.setString("templateParam","{\"code\":\""+random+"\"}");
                return map;
            }
        });
    }

    @Override
    public void add(User user, String smscode) {
        String code = (String) redisTemplate.boundValueOps(user.getPhone()).get();
        if (null==code){
            throw new RuntimeException("验证码已过期");
        }else if (smscode==code){
            user.setCreated(new Date());
            user.setUpdated(new Date());
            userDao.insertSelective(user);
        }else {
            throw new RuntimeException("验证码不正确");
        }

    }

    @Override
    public List<User>findAll() {

      return  userDao.selectByExample(null);

    }

    @Override
    public PageResult search(Integer page, Integer rows, User user) {
        PageHelper.startPage(page,rows);
        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        if (null!=user){
            if (null!=user.getUsername()&& !"".equals(user.getUsername().trim())){
                criteria.andNameLike("%"+user.getUsername()+"%");
            }

        }
         if (null!=user){
             if (null!=user.getPhone() && !"".equals(user.getPhone().trim())){
                 criteria.andPhoneLike("%"+user.getPhone()+"%");
             }
         }

        Page<User> users = (Page<User>) userDao.selectByExample(userQuery);
        PageResult pageResult = new PageResult(users.getTotal(), users.getResult());

        return  pageResult;
    }
}
