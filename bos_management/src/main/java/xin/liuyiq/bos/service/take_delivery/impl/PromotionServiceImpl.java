package xin.liuyiq.bos.service.take_delivery.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.take_delivery.PromotionRepository;
import xin.liuyiq.bos.domain.take_delivery.PageBean;
import xin.liuyiq.bos.domain.take_delivery.Promotion;
import xin.liuyiq.bos.service.take_delivery.PromotionService;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService{

	@Autowired
	private PromotionRepository promotionRepository;
	
	@Override
	public void save(Promotion promotion) {
		
		promotionRepository.save(promotion);
	}

	@Override
	public Page findAll(Pageable pageable) {
		return promotionRepository.findAll(pageable);
	}

	@Override
	public PageBean pageQuery(int page, int rows) {
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Promotion> pageData = promotionRepository.findAll(pageable);
		PageBean<Promotion> pageBean = new PageBean<Promotion>();
		pageBean.setTotalCount(pageData.getTotalElements());
		pageBean.setPageData(pageData.getContent());
		return pageBean;
	}

	@Override
	public void updateStatus(Date date) {
		promotionRepository.updateStatus(date);
	}

}
