package xin.liuyiq.bos.mq;

import org.springframework.stereotype.Service;

import xin.liuyiq.bos.utils.MailUtils;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Service("mailCustomer")
public class MailCustomer implements MessageListener {
	
    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;

        try {
            String subject = mapMessage.getString("subject");
            String content = mapMessage.getString("content");
            String to = mapMessage.getString("to");
            MailUtils.sendMail(subject,content,to);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
