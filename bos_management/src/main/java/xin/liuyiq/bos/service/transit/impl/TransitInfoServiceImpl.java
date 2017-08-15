package xin.liuyiq.bos.service.transit.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.take_delivery.WayBillRepository;
import xin.liuyiq.bos.dao.transit.TransitInfoRepository;
import xin.liuyiq.bos.domain.take_delivery.WayBill;
import xin.liuyiq.bos.domain.tranist.TransitInfo;
import xin.liuyiq.bos.service.transit.TransitInfoService;

@Service
@Transactional
public class TransitInfoServiceImpl implements TransitInfoService{
	
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	
	@Autowired
	private WayBillRepository wayBillRepository;
	
	@Override
	public void create(String wayBillIds) {
		
		for (String wayBillId : wayBillIds.split(",")) {
			WayBill wayBill = wayBillRepository.findOne(Integer.parseInt(wayBillId));
			if(wayBill != null && wayBill.getSignStatus() == 1){
				
				TransitInfo transitInfo = new TransitInfo();
				transitInfo.setWayBill(wayBill);
				transitInfo.setStatus("出入库中转");
				transitInfoRepository.save(transitInfo);
				
				wayBill.setSignStatus(2);
			}
		}
	}

	@Override
	public Page<TransitInfo> findAll(Pageable pageable) {
		return transitInfoRepository.findAll(pageable);
	}
}
