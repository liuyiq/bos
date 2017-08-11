package xin.liuyiq.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import xin.liuyiq.bos.domain.base.Area;

public interface AreaService {

	public void batchImport(List<Area> areas);


	public Page<Area> pageQuery(Specification specification, Pageable pageable);


	public List<Area> findAll();
}
