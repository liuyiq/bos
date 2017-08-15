package xin.liuyiq.bos.web.action.transit;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.constants.Contants;
import xin.liuyiq.bos.domain.tranist.TransitInfo;
import xin.liuyiq.bos.service.transit.TransitInfoService;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class TransitInfoAction extends BaseAction<TransitInfo>{

    @Autowired
    private TransitInfoService transitInfoService;
    
    private static final Map<String,Object> result = new HashMap<String,Object>();
    private String wayBillIds;
   
    public void setWayBillIds(String wayBillIds) {
		this.wayBillIds = wayBillIds;
	}
    //transit_create
	@Action(value = "transit_create",results = {@Result(name = "success",type = "json")})
    public String create(){
    	try{
    		transitInfoService.create(wayBillIds);
    		result.put(Contants.SUCCESS, true);
    		result.put(Contants.MSG, "开启中转配送成功");
    	}catch(Exception e){
    		e.printStackTrace();
    		result.put(Contants.SUCCESS, false);
    		result.put(Contants.MSG, "开启中转配送失败");
    	}
		pushObjectToValueStack(result);
        return SUCCESS;
    }
	
	//transit_pageQuery
	@Action(value = "transit_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
		Pageable pageable = new PageRequest(page-1,rows);
		Page<TransitInfo> page = transitInfoService.findAll(pageable);
		responseJsonDataToPage(page);
        return SUCCESS;
    }
}
