package xin.liuyiq.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import xin.liuyiq.bos.domain.base.SubArea;

public interface SubAreaService {

	public void save(SubArea subArea);

	public Page<SubArea> pageQuery(Pageable pageable);

	public Page<SubArea> pageQuery(Specification<SubArea> specification, Pageable pageable);

	public void batchImport(List<SubArea> subAreas);
}
