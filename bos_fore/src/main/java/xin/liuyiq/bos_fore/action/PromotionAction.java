package xin.liuyiq.bos_fore.action;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.constants.Contants;
import xin.liuyiq.bos.domain.take_delivery.PageBean;
import xin.liuyiq.bos.domain.take_delivery.Promotion;
import xin.liuyiq.bos_fore.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PromotionAction extends BaseAction<Promotion> {

	// promotion_pageQuery.action
	@Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		//
		// 请求bos系统得到promotion中的内容 GET请求
		// 参数page,rows
		// 返回的是pageData/totalCount
		// 把这两参数封装到pageBean对象中传递
		PageBean<Promotion> pageBean = WebClient.create(Contants.BOS_MANAGEMENT_URL+"/service/promotionService/pageQuery/"+page+"/"+rows)
				.accept(MediaType.APPLICATION_JSON).get(PageBean.class);
		
		pushObjectToValueStack(pageBean);
		
		return SUCCESS;
	}
}
