package xin.liuyiq.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import xin.liuyiq.bos.domain.take_delivery.WayBill;

public interface WayBillRepository extends JpaRepository<WayBill, Integer>{

	public WayBill findByWayBillNum(String wayBillNum);

}
