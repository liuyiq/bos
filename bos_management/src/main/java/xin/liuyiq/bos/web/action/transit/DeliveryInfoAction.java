package xin.liuyiq.bos.web.action.transit;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.tranist.DeliveryInfo;
import xin.liuyiq.bos.domain.tranist.InOutStorageInfo;
import xin.liuyiq.bos.service.transit.DeliveryInfoService;
import xin.liuyiq.bos.service.transit.InOutStorageInfoService;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class DeliveryInfoAction extends BaseAction<DeliveryInfo>{
	
	@Autowired
	private DeliveryInfoService deliveryInfoService;
	
	private String transitInfoId;
	
	public void setTransitInfoId(String transitInfoId) {
		this.transitInfoId = transitInfoId;
	}
	
	// delivery_save
	@Action(value = "delivery_save",results = {@Result(name = "success",type = "redirect",location="./pages/transit/transitinfo.html")})
    public String save(){
		deliveryInfoService.save(transitInfoId,model);
		return SUCCESS;
    }
}
