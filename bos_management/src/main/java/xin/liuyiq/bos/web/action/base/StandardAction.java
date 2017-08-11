package xin.liuyiq.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.base.Standard;
import xin.liuyiq.bos.service.base.StandardService;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class StandardAction extends BaseAction<Standard>{
	
	@Autowired
	private StandardService standardService;
	
	@Action(value="standard_save",results={@Result(name="success",type="redirect",location="./pages/base/standard.html")})
	public String saveStandard(){
		standardService.saveStandard(model);
		return SUCCESS;
	}
	
	
	@Action(value="standard_pageQuery",results = {@Result(name = SUCCESS,type = "json")})
	public String pageQuery(){
		// 创建一个分页对象
		Pageable pageable = new PageRequest(page-1,rows);
		// 创建一个条件对象
		Specification specification = new Specification(){

			@Override
			public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
				Predicate predicate = cb.notEqual(root.get("deleteFlag").as(Integer.class), 1);
				return cb.and(predicate);
			}
			
		};
		
		Page<Standard> pageBean = standardService.pageQuery(specification,pageable);
		
		responseJsonDataToPage(pageBean);
		return SUCCESS;
	}
	@Action(value = "standard_findAll",results = {@Result(name = SUCCESS,type = "json")})
	public String findAll() throws IOException {
		List<Standard> standards = standardService.findAll();
		pushObjectToValueStack(standards);
		return SUCCESS;
	}


	private String ids;
	
	public void setIds(String ids) {
		this.ids = ids;
	}
	// standard_delete
	@Action(value = "standard_delete",results={@Result(name="success",type="redirect",location="./pages/base/standard.html")})
	public String delete(){
		String[] idArray = ids.split(",");
		standardService.delete(idArray);
		return SUCCESS ;
	}
	
	// standard_back 恢复收派标准s
	@Action(value = "standard_back",results={@Result(name="success",type="redirect",location="./pages/base/standard.html")})
	public String back(){
		standardService.back();
		return SUCCESS ;
	}
	
	
	
}
