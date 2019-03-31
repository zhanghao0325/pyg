package cn.itcast.core.listener;

import cn.itcast.core.pojo.item.Item;
import com.pinyougou.sellergoods.service.ItemPageService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class PageListener implements MessageListener {
    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage atm = (ActiveMQTextMessage) message;
        try {
            String id = atm.getText();
            itemPageService.index(Long.valueOf(id));

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
