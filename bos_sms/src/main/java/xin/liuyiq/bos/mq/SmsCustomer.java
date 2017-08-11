package xin.liuyiq.bos.mq;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

import xin.liuyiq.bos.utils.SMSUtils;

@Service("smsCustomer")
public class SmsCustomer implements MessageListener{

	@Override
	public void onMessage(Message message) {
		MapMessage mapMessage = (MapMessage) message;
		try {
			String to = mapMessage.getString("telephone");
			String checkCode = mapMessage.getString("code");
			SMSUtils.execute(to, checkCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
