package xin.liuyiq.bos.service.base.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.base.CourierRepository;
import xin.liuyiq.bos.domain.base.Courier;
import xin.liuyiq.bos.service.base.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepository courierRepository;


    @Override
    public void save(Courier courier) {
        courierRepository.save(courier);
    }

	@Override
	public Page<Courier> findAll(Specification specification, Pageable pageable) {
		Page<Courier> page = courierRepository.findAll(specification, pageable);
		return page;
	}

	@Override
	public void deleteBatch(String[] idArr) {
		for (String ids : idArr) {
			Integer id = new Integer(ids);
			courierRepository.updateDeltag(id);
		}
	}

	@Override
	public void restoreBatch(String[] idArr) {
		for (String ids : idArr) {
			Integer id = new Integer(ids);
			courierRepository.updateDeltagToNull(id);
		}
	}

	@Override
	public List<Courier> findNoAssociation() {
		// 构造一个条件
		Specification specification = new Specification() {
			@Override
			public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                Predicate p = cb.isEmpty(root.get("fixedAreas").as(Set.class));
                return p;
			}

		};
        List<Courier> list = courierRepository.findAll(specification);
        return list;
	}
}
