package xin.liuyiq.bos_fore.action;

import xin.liuyiq.bos.domain.constants.Contants;
import xin.liuyiq.bos_fore.common.BaseAction;
import xin.liuyiq.crm.domain.Customer;
import xin.liuyiq.utils.sms.SMSUtils;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class LoginAction extends BaseAction<Customer> {

	private String checkCode;
	private String codeTelephone;
	private String flag;
	
	private final Map<String,Object> result = new HashMap<String,Object>();
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public void setCodeTelephone(String codeTelephone) {
		this.codeTelephone = codeTelephone;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	// customer_login
	@Action(value = "customer_login", results = {
			@Result(name = "success", type = "json")})
	public String login() {
		
		if (flag.equals("1")) {
			if(StringUtils.isBlank(model.getTelephone()) || StringUtils.isBlank(model.getPassword())){
				resultPut(false,"用户名或密码不能为空");
				return SUCCESS; 
			}
			// 帐号密码登录
			Customer customer = WebClient
					.create(Contants.CRM_MANAGEMENT_URL+"/service/customerService/customer/login/"
							+ model.getTelephone() + "/" + model.getPassword())
					.accept(MediaType.APPLICATION_JSON).get(Customer.class);

			if (customer == null) {
				resultPut(false,"用户名或者密码输入错误");
			} else {
				ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
				resultPut(true);
			}
		} else {
			if(StringUtils.isBlank(model.getTelephone())){
				resultPut(false,"手机号输入非法,请重新输入");
				return SUCCESS; 
			}
			String loginCheckCode = (String) ServletActionContext.getRequest().getSession()
					.getAttribute("loginCheckCode");
			if(checkCode.equals(loginCheckCode)){
				resultPut(true);
			}else{
				resultPut(false,"验证码输入错误");
			}
		}
		return SUCCESS;
	}

	public void resultPut(Boolean flag,String msg) {
		this.result.put(Contants.SUCCESS, flag);
		this.result.put(Contants.MSG, msg);
		pushObjectToValueStack(this.result);
	}
	
	public void resultPut(Boolean flag) {
		this.result.put(Contants.SUCCESS, flag);
		pushObjectToValueStack(this.result);
	};

	// 手机发短信
	@Action(value = "cutomer_sendSMS")
	public String sendSMS() {
		String loginCheckCode = RandomStringUtils.randomNumeric(6);
		ServletActionContext.getRequest().getSession().setAttribute("loginCheckCode", loginCheckCode);
		boolean result = SMSUtils.execute(codeTelephone, loginCheckCode);
		return NONE;
	};
	
}