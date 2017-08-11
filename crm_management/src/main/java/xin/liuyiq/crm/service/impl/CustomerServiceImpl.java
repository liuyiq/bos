package xin.liuyiq.crm.service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.crm.dao.CustomerRepository;
import xin.liuyiq.crm.domain.Customer;
import xin.liuyiq.crm.service.CustomerService;
import xin.liuyiq.crm.utils.MD5Utils;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public List<Customer> findNoAssociationCustomers() {
		return customerRepository.findByFixedAreaIdIsNull();
	}

	@Override
	public List<Customer> findHasAssociationFixedAreaCustomers(String fixedAreaId) {
		return customerRepository.findByFixedAreaId(fixedAreaId);
	}

	@Override
	public void associationCustomersToFixedArea(String customerIdStr, String fixedAreaId) {
		customerRepository.clearFixedAreaId(fixedAreaId);
		
		if("null".equals(customerIdStr)){	
			return;   
		}
		
		String[] strings = customerIdStr.split(",");
		for (String idStr : strings) {
			Integer id = Integer.parseInt(idStr);
			customerRepository.updateFixedAreaId(fixedAreaId,id);
		}
	}

	@Override
	public void saveCustomer(Customer customer) {
        String password = customer.getPassword();
        try {
            String newPassword = MD5Utils.getPassword(password);
            customer.setPassword(newPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        customerRepository.save(customer);
	}

	@Override
	public Customer findByTelephone(String telephone) {
		return customerRepository.findByTelephone(telephone);
	}

	@Override
	public void updateTypeByTelephone(String telephone) {
		customerRepository.updateTypeByTelephone(telephone);
		
	}

	@Override
	public Customer findByTelephoneAndPassword(String telephone, String password) {
        try {
            String newPassword = MD5Utils.getPassword(password);
            return customerRepository.findByTelephoneAndPassword(telephone,newPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
	}

	@Override
	public String findFixedAreaIdByAddress(String address) {
		return customerRepository.findFixedAreaIdByAddress(address);
	}

}
