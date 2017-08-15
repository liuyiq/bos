package xin.liuyiq.bos.service.transit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import xin.liuyiq.bos.domain.tranist.TransitInfo;

public interface TransitInfoService {

	public void create(String wayBillIds);

	public Page<TransitInfo> findAll(Pageable pageable);

}
