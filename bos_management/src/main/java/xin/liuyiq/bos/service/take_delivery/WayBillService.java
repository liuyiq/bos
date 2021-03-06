package xin.liuyiq.bos.service.take_delivery;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import xin.liuyiq.bos.domain.take_delivery.WayBill;

public interface WayBillService {

	public void save(WayBill model);

	public Page<WayBill> pageQuery(WayBill wayBill ,Pageable pageable);

	public WayBill findByWayBillNum(String wayBillNum);

	public void syncIndex();

	public List<WayBill> findAll(WayBill model);

}
