package xin.liuyiq.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import xin.liuyiq.bos.domain.base.Standard;

public interface StandardService {

	public void saveStandard(Standard standard);

	public Page<Standard> pageQuery(Specification specification, Pageable pageable);

	public List<Standard> findAll();

	public void delete(String[] idArray);

	public void back();

}
