package xin.liuyiq.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.base.StandardRepository;
import xin.liuyiq.bos.domain.base.Standard;
import xin.liuyiq.bos.service.base.StandardService;

@Service
@Transactional
public class StandardServiceImpl implements StandardService{
	
	@Autowired
	private StandardRepository standardRepository;
	
	@Override
	public void saveStandard(Standard standard) {
		standardRepository.save(standard);
	}


	@Override
	public List<Standard> findAll() {
		List<Standard> standards = standardRepository.findAll();
		return standards;
	}

	@Override
	public void delete(String[] idArray) {
		for (String idStr : idArray) {
			Integer id = new Integer(idStr);
			Standard standard = standardRepository.findOne(id);
			standard.setDeleteFlag(1);
		}
	}


	@Override
	public Page<Standard> pageQuery(Specification specification, Pageable pageable) {
		
		return standardRepository.findAll(specification, pageable);
	}


	@Override
	public void back() {
		List<Standard> standardList = standardRepository.findAll();
		for (Standard standard : standardList) {
			if(standard.getDeleteFlag()==1){
				standard.setDeleteFlag(0);
			}
		}
	}


}
