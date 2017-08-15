package xin.liuyiq.bos.service.transit.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.transit.InOutStorageInfoRepository;
import xin.liuyiq.bos.dao.transit.TransitInfoRepository;
import xin.liuyiq.bos.domain.tranist.InOutStorageInfo;
import xin.liuyiq.bos.domain.tranist.TransitInfo;
import xin.liuyiq.bos.service.transit.InOutStorageInfoService;

@Service
@Transactional
public class InOutStorageInfoServiceImpl implements InOutStorageInfoService{

	@Autowired
	private TransitInfoRepository transitInfoRepository;
	
	@Autowired
	private InOutStorageInfoRepository inOutStorageInfoRepository;
	
	@Override
	public void save(String transitInfoId, InOutStorageInfo inOutStorageInfo) {
		inOutStorageInfoRepository.save(inOutStorageInfo);
		
		// 通过transitInfoId来查询运输配送信息
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
		// 关联出入库信息
		transitInfo.getInOutStorageInfos().add(inOutStorageInfo);
		// 修改状态
		if(inOutStorageInfo.getOperation().equals("到达网点")){
			transitInfo.setStatus("到达网点");
			transitInfo.setOutletAddress(inOutStorageInfo.getAddress());
		}
	}

}
