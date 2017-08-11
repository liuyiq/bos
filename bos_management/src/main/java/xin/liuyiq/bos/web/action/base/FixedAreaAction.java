package xin.liuyiq.bos.web.action.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
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

import xin.liuyiq.bos.domain.base.FixedArea;
import xin.liuyiq.bos.service.base.FixedAreaService;
import xin.liuyiq.bos.web.action.common.BaseAction;
import xin.liuyiq.crm.domain.Customer;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class FixedAreaAction extends BaseAction<FixedArea> {

	@Autowired
	private FixedAreaService fixedAreaService;

	// fixedArea_save
	@Action(value = "fixedArea_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html") })
	public String save() {
		fixedAreaService.save(model);
		return SUCCESS;
	}

	// fixedArea_pageQuery
	@Action(value = "fixedArea_pageQuery",results={@Result(name=SUCCESS,type="json")})
	public String pageQuery() {
		// 创建分页对象
		Pageable pageable = new PageRequest(page - 1, rows);
		// 创建条件对象
		Specification specification = new Specification() {
			@Override
			public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
				// 判断条件
				if (StringUtils.isNotBlank(model.getId())) {
					Predicate p1 = cb.like(root.get("id").as(String.class), "%" + model.getId() + "%");
					predicateList.add(p1);
				}
				if (StringUtils.isNotBlank(model.getCompany())) {
					Predicate p2 = cb.like(root.get("company").as(String.class), "%" + model.getCompany() + "%");
					predicateList.add(p2);
				}

				return cb.and(predicateList.toArray(new Predicate[0]));
			}
		};

		Page result = fixedAreaService.pageQuery(specification, pageable);

		// 返回数据json
		responseJsonDataToPage(result);
		
		return SUCCESS;
	}

	// fixedArea_findNoAssociationCustomers
	@Action(value = "fixedArea_findNoAssociationCustomers",results={@Result(name=SUCCESS,type="json")})
	public String findNoAssociationCustomers() {
		
		Collection<? extends Customer> list = fixedAreaService.findNoAssociationCustomers();
		
		pushObjectToValueStack(list);
		
		return SUCCESS;
	}
	
	// fixedArea_findAssociationCustomers
	@Action(value = "fixedArea_findAssociationCustomers",results={@Result(name=SUCCESS,type="json")})
	public String findAssociationCustomers() {
		
		Collection<? extends Customer> list = fixedAreaService.findAssociationCustomers(model.getId());
		
		pushObjectToValueStack(list);
		
		return SUCCESS;
	}
	
	// fixedArea_associationCustomersToFixedArea
	private String[] customerIds;

	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	@Action(value = "fixedArea_associationCustomersToFixedArea",results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html") })
	public String associationCustomersToFixedArea() {
		// 需要两个参数
		String customerIdStr = StringUtils.join(customerIds,",");
		fixedAreaService.associationCustomersToFixedArea(customerIdStr,model.getId());
		
		return SUCCESS;
	}

	private Integer courierId;
	private Integer takeTimeId;

	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}

	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}

	// fixedarea_associationCourierToFixedArea
	@Action(value="fixedarea_associationCourierToFixedArea",results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html") })
	public String associationCourierToFixedArea(){
		fixedAreaService.associationCourierToFixedArea(model.getId(),courierId,takeTimeId);
		return SUCCESS;
	}
	
	// fixedArea_findAll
	@Action(value="fixedArea_findAll",results = {
			@Result(name = "success", type = "json") })
	public String findAll(){
		List<FixedArea> list = fixedAreaService.findAll();
		pushObjectToValueStack(list);
		return SUCCESS;
	}
}
