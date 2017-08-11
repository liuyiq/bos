package xin.liuyiq.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.base.SubAreaRepository;
import xin.liuyiq.bos.domain.base.SubArea;
import xin.liuyiq.bos.service.base.SubAreaService;

@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService{
	
	@Autowired
	private SubAreaRepository subAreaRepository;
	
	@Override
	public void save(SubArea subArea) {
		subAreaRepository.save(subArea);
	}

	@Override
	public Page<SubArea> pageQuery(Pageable pageable) {
		return subAreaRepository.findAll(pageable);
	}

	@Override
	public Page<SubArea> pageQuery(Specification<SubArea> specification, Pageable pageable) {
		return subAreaRepository.findAll(specification, pageable);
	}

	@Override
	public void batchImport(List<SubArea> subAreas) {
		subAreaRepository.save(subAreas);
	}
}
