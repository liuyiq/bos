package xin.liuyiq.bos.web.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.constants.Contants;
import xin.liuyiq.bos.domain.take_delivery.Order;
import xin.liuyiq.bos.service.take_delivery.OrderService;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class OrderAction extends BaseAction<Order>{
	
	private final Map<String,Object> result = new HashMap<String,Object>();
	@Autowired
	private OrderService orderService;

	@Action(value="order_findByOrderNum",results={@Result(name="success",type="json")})
	public String findByOrderNum(){
		Order order = orderService.findByOrderNum(model.getOrderNum());
		
		if(order != null){
			// 订单是存在的
			this.result.put(Contants.SUCCESS, true);
			this.result.put(Contants.ORDER_DATA, order);
		}else{
			// 订单是不存在的
			this.result.put(Contants.SUCCESS, false);
		}
		pushObjectToValueStack(this.result);
		
		
		return SUCCESS;
	}

}
