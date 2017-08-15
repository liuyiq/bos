package xin.liuyiq.bos.web.action.transit;

import javax.inject.Named;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.tranist.SignInfo;
import xin.liuyiq.bos.service.transit.SignInfoService;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class SignAction extends BaseAction<SignInfo>{
	
	@Autowired
	private SignInfoService signInfoService;
	
	private String transitInfoId;
	
	public void setTransitInfoId(String transitInfoId) {
		this.transitInfoId = transitInfoId;
	}

	@Action(value = "sign_save",results = {@Result(name = "success",type = "redirect",location="./pages/transit/transitinfo.html")})
	public String save(){
		signInfoService.save(transitInfoId,model);
		return SUCCESS;
	}
}
