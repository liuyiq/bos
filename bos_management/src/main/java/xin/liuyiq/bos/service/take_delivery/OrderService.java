package xin.liuyiq.bos.service.take_delivery;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import xin.liuyiq.bos.domain.take_delivery.Order;

public interface OrderService {
	
	@POST
	@Path("/order")
	@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public void save(Order order);

	public Order findByOrderNum(String orderNum);
}
