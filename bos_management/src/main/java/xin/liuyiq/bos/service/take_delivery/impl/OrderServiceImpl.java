package xin.liuyiq.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.base.AreaRepository;
import xin.liuyiq.bos.dao.base.FixedAreaRepository;
import xin.liuyiq.bos.dao.take_delivery.OrderRepository;
import xin.liuyiq.bos.dao.take_delivery.WorkBillRepository;
import xin.liuyiq.bos.domain.base.Area;
import xin.liuyiq.bos.domain.base.Courier;
import xin.liuyiq.bos.domain.base.FixedArea;
import xin.liuyiq.bos.domain.base.SubArea;
import xin.liuyiq.bos.domain.constants.Contants;
import xin.liuyiq.bos.domain.take_delivery.Order;
import xin.liuyiq.bos.domain.take_delivery.WorkBill;
import xin.liuyiq.bos.service.take_delivery.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private FixedAreaRepository fixedAreaRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private WorkBillRepository workBillRepository;

	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;

	@Override
	public void save(Order order) {
		// 订单编号
		order.setOrderNum(UUID.randomUUID().toString().replace("-", ""));
		// 下单时间
		order.setOrderTime(new Date());
		// 通过省市区获取到了定区,然后进行发送地址和分区关键字的匹配,来找到快递员
		// 寄件人的区域
		Area sendArea = order.getSendArea();
		Area persistSendArea = areaRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(), sendArea.getCity(),
				sendArea.getDistrict());
		order.setSendArea(persistSendArea);
		// 收件人的区域
		Area recArea = order.getRecArea();
		Area persistRecArea = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(), recArea.getCity(),
				recArea.getDistrict());
		order.setRecArea(persistRecArea);
		String fixedAreaId = WebClient.create(Contants.CRM_MANAGEMENT_URL
				+ "/service/customerService/customer/findfixedareaidbyaddress?address="
				+ order.getSendAddress()).accept(MediaType.APPLICATION_JSON).get(String.class);
		// 通过fixedAreaId查询到
		if (fixedAreaId != null) {
			// 通过fixedAreaId去查询定区
			FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
			// 获得定区中的快递员然后进行保存操作
			if (findCourierByFixedArea(fixedArea, order)) {
				return;
			}
		}

		// 通过分区找关键字 persistSendArea
		for (SubArea subArea : persistSendArea.getSubareas()) {
			// 得到了每一个分区
			if (order.getSendAddress().contains(subArea.getKeyWords())) {
				// 通过分区获取的定区
				FixedArea fixedArea = subArea.getFixedArea();
				// 获得定区中的快递员然后进行保存操作
				if (findCourierByFixedArea(fixedArea, order)) {
					return;
				}
			}
		}

		// 通过分区找辅助关键字 persistSendArea
		// 也可以和查找关键字合并,看需求了
		for (SubArea subArea : persistSendArea.getSubareas()) {
			// 得到了每一个分区
			if (order.getSendAddress().contains(subArea.getAssistKeyWords())) {
				// 通过分区获取的定区
				FixedArea fixedArea = subArea.getFixedArea();
				// 获得定区中的快递员然后进行保存操作
				if (findCourierByFixedArea(fixedArea, order)) {
					return;
				}
			}
		}

		// 人工分单的方法
		saveOrder(order);
	}

	// 获取到定区后进行的操作
	private boolean findCourierByFixedArea(FixedArea fixedArea, Order order) {
		Iterator<Courier> iterator = fixedArea.getCouriers().iterator();
		if (iterator.hasNext()) {
			// 获取到定区中有快递员
			Courier courier = iterator.next();
			if (courier != null) {
				saveOrder(order, courier);
				// 发短信 生成工单
				generateWorkBill(order);
				return true;
			}
		}
		return false;
	}

	// 生成工单
	private void generateWorkBill(final Order order) {
		WorkBill workBill = new WorkBill();
		workBill.setType("新");
		workBill.setPickstate("新单");
		workBill.setBuildtime(new Date());
		workBill.setRemark(order.getRemark());
		final String smsNumber = RandomStringUtils.randomNumeric(4);
		workBill.setSmsNumber(smsNumber);
		workBill.setOrder(order);
		workBill.setCourier(order.getCourier());
		workBillRepository.save(workBill);
		
		/**
		 * 这是给快递员发短信的方法,把发短信交给队列"courier_sms"
		 * 下面的功能未实现
		 * 在bos_sms系统中,写个方法消费队列中的消息
		 * 没有实现该功能 是因为要在SMSUtils中还要增加短信模版
		 */
		/*jmsTemplate.send("courier_sms", new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = (MapMessage) session;
				mapMessage.setString("telephone", order.getCourier().getTelephone());
				String msg = "短信编号" + smsNumber + ",联系人" + order.getSendName() + ",电话" + order.getSendMobile() + ",地址"
						+ order.getSendAddress() + ",给快递员捎话" + order.getSendMobileMsg();
				mapMessage.setString("msg", msg);
				return mapMessage;
			}
		});*/
		
		System.out.println("短信发送成功");
		
		// 修改取件状态
		workBill.setPickstate("已通知");
	}

	// 自动分单成功后保存订单的方法
	private void saveOrder(Order order, Courier courier) {
		// 设置订单的快递员
		order.setCourier(courier);
		// 分单状态--自动分单
		order.setOrderType("1");
		// 保存订单
		orderRepository.save(order);
	}

	// 人工分单保存订单的方法
	private void saveOrder(Order order) {
		// 分单状态--人工分单
		order.setOrderType("2");
		// 保存订单
		orderRepository.save(order);
	}

	@Override
	public Order findByOrderNum(String orderNum) {
		return orderRepository.findByOrderNum(orderNum);
	}

}
