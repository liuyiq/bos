package xin.liuyiq.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import xin.liuyiq.bos.domain.base.Courier;

public interface CourierService {

	public void save(Courier courier);

	public Page<Courier> findAll(Specification specification, Pageable pageable);

	public void deleteBatch(String[] idArr);

    public void restoreBatch(String[] idArr);

    public List<Courier> findNoAssociation();
}
