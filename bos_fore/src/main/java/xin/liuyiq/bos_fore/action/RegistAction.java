package xin.liuyiq.bos_fore.action;

import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.constants.Contants;
import xin.liuyiq.bos_fore.common.BaseAction;
import xin.liuyiq.crm.domain.Customer;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class RegistAction extends BaseAction<Customer> {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private JmsTemplate jmsQueueTemplate;

	@Action(value = "checkCode", results = { @Result(name = "ERROR", type = "json") })
	public String checkCode() {
		final String code = RandomStringUtils.randomNumeric(6);
		// 把这个随机的验证码发送到用户的手机上
		ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), code);
		
		// 第一个参数是队列的名称
		jmsQueueTemplate.send("bos_sms", new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", model.getTelephone());
				mapMessage.setString("code",code);
				return mapMessage;
			}
		});

		return NONE;
	}

	// checkCodeAndSession
	@Action(value = "checkCodeAndSession", results = { @Result(name = "success", type = "json") })
	public String checkCodeAndSession() {
		String checkCodeSession = (String) ServletActionContext.getRequest().getSession()
				.getAttribute(model.getTelephone());
		pushObjectToValueStack(checkCodeSession);
		return SUCCESS;
	}

	// regist
	@Action(value = "regist", results = {
			@Result(name = "success", type = "redirect", location = "./signup-success.html") })
	public String regist() {
		WebClient.create(Contants.CRM_MANAGEMENT_URL+"/service/customerService/saveCustomer").post(model);

		// 发送一封邮件
		String activeCode = RandomStringUtils.randomNumeric(32);
		
		// 把这个激活码存到redis中
		redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 24, TimeUnit.DAYS);

		final String subject = "速运快递的激活邮件";// 主题
		String href = Contants.BOS_FORE_URL+"/activeEmailAction?telephone=" + model.getTelephone()
				+ "&activeCode=" + activeCode; // 邮件的连接地址
		final String content = "<h3>你好,感谢您注册速运快递,请点击下面链接完成邮箱验证</br><a href=" + href + ">" + href
				+ "</a></br>为保障您的账户安全，请在24小时内点击该链接，您也可以将链接复制到浏览器地址栏访问</h3>"; // 邮件内容
		final String to = model.getEmail(); // 收件人

		// MailUtils.sendMail(subject, content, to);
		jmsQueueTemplate.send("bos_mail", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage message = session.createMapMessage();
                message.setString("subject",subject);
                message.setString("content",content);
                message.setString("to",to);
                return message;
            }
        });
        return SUCCESS;
	}

	private String activeCode;

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	// 邮箱激活操作的activeEmailAction
	@Action(value = "activeEmailAction", results = {
			@Result(name = "success", type = "redirect", location = "login.html") })
	public String activeEmail() throws Exception {
		// 通过唯一标识手机号,获取到redis中的激活码
		String activeCodeInRedis = redisTemplate.opsForValue().get(model.getTelephone());
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		/**
		 * 判断是否为空或和页面返回的激活码是否一致 如果过一致 去crm 系统调用一次查询状态码的接口 返回是1 就说明已经激活绑定了邮箱
		 * 提示已经激活 如果不是1 没有绑定邮箱 就调一次crm的接口把状态码改成1 不一致就提示过期
		 */
		if (activeCodeInRedis == null || !activeCodeInRedis.equals(activeCode)) {
			// 提示过期
			ServletActionContext.getResponse().getWriter().println("激活码过期,请重新注册");
		} else {
			// 去crm 系统调用一次查询状态码的接口 通过手机号码查询该用户的信息
			// getConllecion 能够返回一个集合 但是这个地方用不到,会有个bug 就是手机号码需要在注册前进行一个校验
			// 手机号码在注册的时候也是不能重复的

			Customer customer = WebClient
					.create(Contants.CRM_MANAGEMENT_URL+"/service/customerService/customer/telephone/"
							+ model.getTelephone())
					.accept(MediaType.APPLICATION_JSON).get(Customer.class);
			if (customer.getType() == null || customer.getType() != 1) {
				// 未绑定操作
				WebClient.create(Contants.CRM_MANAGEMENT_URL+"/service/customerService/customer/updatetype/"
						+ model.getTelephone()).put(null);
				// 提示激活成功
				ServletActionContext.getResponse().getWriter().println("激活成功");
				return SUCCESS;
			} else {
				// 提示已经激活
				ServletActionContext.getResponse().getWriter().println("你已经激活成功,请勿重复激活");
			}
			// 删除激活码
			redisTemplate.delete(model.getTelephone());
		}

		return NONE;
	}
}
