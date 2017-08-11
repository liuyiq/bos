package xin.liuyiq.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import xin.liuyiq.bos.domain.take_delivery.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>,JpaSpecificationExecutor<Order>{

	Order findByOrderNum(String orderNum);

}
