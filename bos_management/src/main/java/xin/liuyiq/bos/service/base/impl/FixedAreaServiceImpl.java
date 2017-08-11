package xin.liuyiq.bos.service.base.impl;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.base.CourierRepository;
import xin.liuyiq.bos.dao.base.FixedAreaRepository;
import xin.liuyiq.bos.dao.base.TakeTimeRepository;
import xin.liuyiq.bos.domain.base.Courier;
import xin.liuyiq.bos.domain.base.FixedArea;
import xin.liuyiq.bos.domain.base.TakeTime;
import xin.liuyiq.bos.domain.constants.Contants;
import xin.liuyiq.bos.service.base.FixedAreaService;
import xin.liuyiq.crm.domain.Customer;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {
	
	@Autowired
	private FixedAreaRepository fixedAreaRepository;

	@Override
	public void save(FixedArea fixedArea) {
		fixedAreaRepository.save(fixedArea);
	}

	@Override
	public Page pageQuery(Specification specification, Pageable pageable) {
		return fixedAreaRepository.findAll(specification, pageable);
	}

	@Override
	public Collection<? extends Customer> findNoAssociationCustomers() {

		Collection<? extends Customer> collection = WebClient
				.create(Contants.CRM_MANAGEMENT_URL + "/service/customerService/noassociationcustomers")
				.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		return collection;
	}

	@Override
	public Collection<? extends Customer> findAssociationCustomers(String id) {
		Collection<? extends Customer> collection = WebClient
				.create(Contants.CRM_MANAGEMENT_URL
						+ "/service/customerService/associationfixedareacustomers/" + id)
				.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		return collection;
	}

	@Override
	public void associationCustomersToFixedArea(String customerIds, String id) {

		WebClient.create(Contants.CRM_MANAGEMENT_URL
				+ "/service/customerService/associationcustomerstofixedarea?customerIdStr=" + customerIds
				+ "&fixedAreaId=" + id).put(null);
	}

	@Autowired
	private CourierRepository courierRepository;
	@Autowired
	private TakeTimeRepository takeTimeRepository;

	@Override
	public void associationCourierToFixedArea(String id, Integer courierId, Integer takeTimeId) {
		// 要在这个里面查询出来courier,taketime,fixedarea
		// 定区
		FixedArea fixedArea = fixedAreaRepository.findOne(id);
		// 快递员
		Courier courier = courierRepository.findOne(courierId);
		// 收派时间
		TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
		// 然而他们三个的关系是一个快递员中有一个收派时间
		courier.setTakeTime(takeTime);
		// 在定区中有一个快递员的引用
		fixedArea.getCouriers().add(courier);
	}

	@Override
	public List<FixedArea> findAll() {
		return fixedAreaRepository.findAll();
	}

}
