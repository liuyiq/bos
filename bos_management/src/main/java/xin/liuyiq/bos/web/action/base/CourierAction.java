package xin.liuyiq.bos.web.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
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

import com.opensymphony.xwork2.ActionContext;

import xin.liuyiq.bos.domain.base.Courier;
import xin.liuyiq.bos.service.base.CourierService;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CourierAction extends BaseAction<Courier> {

	@Autowired
	private CourierService courierService;


	private Specification specification;


	@Action(value = "courier_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/courier.html") })
	public String save() {
		courierService.save(model);
		return SUCCESS;
	}

	@Action(value = "courier_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 封装一个specification
		Specification specification = new Specification() {
			List<Predicate> predicateList = new ArrayList<Predicate>();

			@Override
			public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
				// courierNum
				if (StringUtils.isNotBlank(model.getCourierNum())) {
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), model.getCourierNum());
					predicateList.add(p1);
				}

				if (StringUtils.isNotBlank(model.getCompany())) {
					//
					// courier.getCompany() 这是 从网页上传来的数据
					Predicate p2 = cb.like(root.get("company").as(String.class), "%" + model.getCompany() + "%");
					predicateList.add(p2);
				}

				if (StringUtils.isNoneBlank(model.getType())) {
					Predicate p3 = cb.equal(root.get("type").as(String.class), model.getType());
					predicateList.add(p3);
				}

				// standard.name
				Join standardRoot = root.join("standard", JoinType.INNER);
				if (model.getStandard() != null && StringUtils.isNotBlank(model.getStandard().getName())) {
					Predicate p4 = cb.like(standardRoot.get("name").as(String.class),
							"%" + model.getStandard().getName() + "%");
					predicateList.add(p4);
				}

				//
				return cb.and(predicateList.toArray(new Predicate[0]));
			}

		};

		Pageable pageable = new PageRequest(page - 1, rows);

		Page<Courier> page = courierService.findAll(specification, pageable);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", page.getTotalElements());
		map.put("rows", page.getContent());
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	// courier_deleteBatch
	private String ids;
	
	public void setIds(String ids) {
		this.ids = ids;
	}

	@Action(value="courier_deleteBatch",results={@Result(name="success",location="./pages/base/courier.html",type="redirect")})
	public String deleteBatch(){
		String[] idArr = ids.split(",");

		courierService.deleteBatch(idArr);
		
		return SUCCESS;
	}
	
	// courier_restoreBatch
	@Action(value="courier_restoreBatch",results={@Result(name="success",location="./pages/base/courier.html",type="redirect")})
	public String restoreBatch(){
		String[] idArr = ids.split(",");

		courierService.restoreBatch(idArr);

		return SUCCESS;
	}

	// courier_findnoassociation
	@Action(value="courier_findnoassociation",results={@Result(name="success",type="json")})
	public String findNoAssociation(){
		List<Courier> list = courierService.findNoAssociation();
		pushObjectToValueStack(list);
		return SUCCESS;
	}


}
