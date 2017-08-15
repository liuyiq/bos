package xin.liuyiq.bos.service.transit.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.transit.SignInfoRepository;
import xin.liuyiq.bos.dao.transit.TransitInfoRepository;
import xin.liuyiq.bos.domain.tranist.SignInfo;
import xin.liuyiq.bos.domain.tranist.TransitInfo;
import xin.liuyiq.bos.service.transit.SignInfoService;

@Service
@Transactional
public class SignInfoServiceImpl implements SignInfoService{
	
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	
	@Autowired
	private SignInfoRepository signInfoRepository;
	
	@Override
	public void save(String transitInfoId, SignInfo signInfo) {
		signInfoRepository.save(signInfo);
		
		// 签收 更改的状态 
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
		transitInfo.setSignInfo(signInfo);
		
		// 是否签收
		if("正常".equals(signInfo.getSignType())){
			transitInfo.setStatus("正常签收");
			transitInfo.getWayBill().setSignStatus(3);
		}else{
			transitInfo.setStatus("异常");
			transitInfo.getWayBill().setSignStatus(4);
		}
		
	}

}
