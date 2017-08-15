package xin.liuyiq.bos.service.transit.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.transit.DeliveryInfoRepository;
import xin.liuyiq.bos.dao.transit.TransitInfoRepository;
import xin.liuyiq.bos.domain.tranist.DeliveryInfo;
import xin.liuyiq.bos.domain.tranist.TransitInfo;
import xin.liuyiq.bos.service.transit.DeliveryInfoService;

@Service
@Transactional
public class DeliveryInfoServiceImpl implements DeliveryInfoService{
	
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	
	@Autowired
	private DeliveryInfoRepository deliveryInfoRepository;
	
	@Override
	public void save(String transitInfoId, DeliveryInfo deliveryInfo) {
		deliveryInfoRepository.save(deliveryInfo);
		
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
		// 设置配送信息和运输状态
		transitInfo.setDeliveryInfo(deliveryInfo);
		transitInfo.setStatus("开始配送");
	}
}
