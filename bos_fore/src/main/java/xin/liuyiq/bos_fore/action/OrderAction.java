package xin.liuyiq.bos_fore.action;

import java.util.Date;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.base.Area;
import xin.liuyiq.bos.domain.constants.Contants;
import xin.liuyiq.bos.domain.take_delivery.Order;
import xin.liuyiq.bos_fore.common.BaseAction;
import xin.liuyiq.crm.domain.Customer;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class OrderAction extends BaseAction<Order> {

	private String sendAreaInfo;
	private String recAreaInfo;

	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}

	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}

	// order_save.action
	@Action(value = "order_save",results={@Result(name="success",location="index.html",type="redirect")})
	public String save() {
		// 设置发件人区域信息
		Area sendArea = new Area();
		String[] sendAreaInfoArray = sendAreaInfo.split("/");
		sendArea.setProvince(sendAreaInfoArray[0]);
		sendArea.setCity(sendAreaInfoArray[1]);
		sendArea.setDistrict(sendAreaInfoArray[2]);
		model.setSendArea(sendArea);
		// 设置 收件人区域信息
		Area recArea = new Area();
		String[] recAreaArray = recAreaInfo.split("/");
		recArea.setProvince(recAreaArray[0]);
		recArea.setCity(recAreaArray[1]);
		recArea.setDistrict(recAreaArray[2]);
		model.setRecArea(recArea);
		// 关联当前用户
		Customer customer  = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
		model.setCustomer_id(customer.getId());
		
		// webService 发到bos系统然后保存order
		WebClient.create(Contants.BOS_MANAGEMENT_URL + "/service/orderService/order")
				.type(MediaType.APPLICATION_JSON).post(model);
		
		return SUCCESS;
	}

}
