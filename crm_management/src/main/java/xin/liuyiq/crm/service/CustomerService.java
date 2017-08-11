package xin.liuyiq.crm.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import xin.liuyiq.crm.domain.Customer;


public interface CustomerService {

	/**
	 * 这里有三个方法
	 * 1.查询没有关联的用户 查询用get
	 * 2.查询已经关联的用户
	 * 3.关联用户 update 用put
	 */
	@GET
	@Path("/noassociationcustomers")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public List<Customer> findNoAssociationCustomers();
	
	
	@GET
	@Path("/associationfixedareacustomers/{fixedareaid}")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public List<Customer> findHasAssociationFixedAreaCustomers(@PathParam("fixedareaid") String fixedAreaId);
	
	
	@PUT
	@Path("/associationcustomerstofixedarea")
	public void associationCustomersToFixedArea(
			@QueryParam("customerIdStr") String customerIdStr,
			@QueryParam("fixedAreaId") String fixedAreaId);
	
	
	// 用户注册后存储到数据库
	@POST
	@Path("/saveCustomer")
	public void saveCustomer(Customer customer);
	
	// 用户在激活邮箱的时候查询用户的状态码是否为1
	@GET
	@Path("/customer/telephone/{telephone}")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Customer findByTelephone(@PathParam("telephone") String telephone);
	
	
	// updatetype 通过手机号码来修改状态码type为1
	@PUT
	@Path("/customer/updatetype/{telephone}")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public void updateTypeByTelephone(@PathParam("telephone") String telephone);

	// login登录功能
	@GET
	@Path("/customer/login/{telephone}/{password}")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Customer findByTelephoneAndPassword(@PathParam("telephone") String telephone,@PathParam("password") String password);

	// 通过地址来查找定区ID
	@GET
	@Path("/customer/findfixedareaidbyaddress")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public String findFixedAreaIdByAddress(@QueryParam("address") String address);

}
