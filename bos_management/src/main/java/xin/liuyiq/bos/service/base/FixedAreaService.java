package xin.liuyiq.bos.service.base;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import xin.liuyiq.bos.domain.base.FixedArea;
import xin.liuyiq.crm.domain.Customer;

public interface FixedAreaService {

    public void save(FixedArea model);

    public Page pageQuery(Specification specification, Pageable pageable);

	public Collection<? extends Customer> findNoAssociationCustomers();

	public Collection<? extends Customer> findAssociationCustomers(String id);

	public void associationCustomersToFixedArea(String customerIds, String id);

    public void associationCourierToFixedArea(String id, Integer courierId, Integer takeTimeId);

	public List<FixedArea> findAll();

}
