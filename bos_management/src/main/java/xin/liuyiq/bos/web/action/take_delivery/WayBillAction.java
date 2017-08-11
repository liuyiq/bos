package xin.liuyiq.bos.web.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.constants.Contants;
import xin.liuyiq.bos.domain.take_delivery.WayBill;
import xin.liuyiq.bos.service.take_delivery.WayBillService;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class WayBillAction extends BaseAction<WayBill> {

	private static final Logger LOGGER = Logger.getLogger(WayBill.class);
	
	private final Map<String, Object> result = new HashMap<String, Object>();

	@Autowired
	private WayBillService wayBillService;

	@Action(value = "waybill_save", results = { @Result(name = "success", type = "json") })
	public String save() {

		try {
			if (model.getOrder() != null && (model.getOrder().getId() == null || model.getOrder().getId() == 0)) {
				// 关联瞬时对象异常
				// 如果order的主键为空或者是0的时候就把order设置为空
				model.setOrder(null);
			}
			wayBillService.save(model);
			this.result.put(Contants.SUCCESS, true);
			this.result.put(Contants.MSG, "运单保存成功");
			LOGGER.info("运单保存成功+运单号:" + model.getWayBillNum());
		} catch (Exception e) {
			e.printStackTrace();
			this.result.put(Contants.SUCCESS, false);
			this.result.put(Contants.MSG, "运单保存失败");
			// 异常信息
			LOGGER.info("运单保存失败+运单号:" + model.getWayBillNum(), e);
		}
		pushObjectToValueStack(this.result);
		return SUCCESS;
	}

	@Action(value = "waybill_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		
		Pageable pageable = new PageRequest(page-1, rows, Direction.ASC,"id");

		Page<WayBill> page = wayBillService.pageQuery(model,pageable);

		responseJsonDataToPage(page);

		return SUCCESS;
	}
	
	@Action(value = "waybill_findByWayBillNum", results = { @Result(name = "success", type = "json") })
	public String findByWayBillNum() {
		WayBill wayBill = wayBillService.findByWayBillNum(model.getWayBillNum());
		if (wayBill != null) {
			// 查询到了运单
			this.result.put(Contants.SUCCESS, true);
			this.result.put(Contants.WAYBILL_DATA, wayBill);

		} else {
			// 没有查到运单
			this.result.put(Contants.SUCCESS, false);
		}
		pushObjectToValueStack(this.result);
		return SUCCESS;
	}
}
