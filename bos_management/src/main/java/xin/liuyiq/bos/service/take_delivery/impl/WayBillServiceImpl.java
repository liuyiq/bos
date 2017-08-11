package xin.liuyiq.bos.service.take_delivery.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.take_delivery.WayBillRepository;
import xin.liuyiq.bos.domain.take_delivery.WayBill;
import xin.liuyiq.bos.index.WayBillIndexRepository;
import xin.liuyiq.bos.service.take_delivery.WayBillService;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {

	@Autowired
	private WayBillRepository wayBillRepository;

	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;

	@Override
	public void save(WayBill wayBill) {
		WayBill persistWayBill = wayBillRepository.findByWayBillNum(wayBill.getWayBillNum());
		try {
			if (persistWayBill == null || persistWayBill.getId() == null) {
				// 首次录入运单时候保存运单
				wayBillRepository.save(wayBill);
				// 保存索引
				wayBillIndexRepository.save(wayBill);
			} else {
				// 修改运单信息后保存运单
				Integer id = persistWayBill.getId();
				BeanUtils.copyProperties(persistWayBill, wayBill);
				persistWayBill.setId(id);
				// 保存索引
				wayBillIndexRepository.save(persistWayBill);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Page<WayBill> pageQuery(WayBill wayBill, Pageable pageable) {
		// 判断查询条件
		if (StringUtils.isBlank(wayBill.getWayBillNum()) && StringUtils.isBlank(wayBill.getSendAddress())
				&& StringUtils.isBlank(wayBill.getRecAddress()) && StringUtils.isBlank(wayBill.getSendProNum())
				&& (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
			// 无条件查询
			return wayBillRepository.findAll(pageable);
		} else {
			BoolQueryBuilder boolQuery = new BoolQueryBuilder();

			// 运单号
			if (StringUtils.isNotBlank(wayBill.getWayBillNum())) {
				QueryBuilder termQueryr = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
				boolQuery.must(termQueryr);
			}

			// 发货地址
			if (StringUtils.isNotBlank(wayBill.getSendAddress())) {
				BoolQueryBuilder query = new BoolQueryBuilder();
				// 默认词条分词
				QueryBuilder wildcardQuery = new WildcardQueryBuilder("sendAddress",
						"*" + wayBill.getSendAddress() + "*");
				query.should(wildcardQuery);
				// 多词条组分陪陪,分词后再匹配
				QueryBuilder stringQueryBuilder = new QueryStringQueryBuilder(wayBill.getSendAddress())
						.field("sendAddress").defaultOperator(Operator.AND);
				query.should(stringQueryBuilder);

				boolQuery.must(query);
			}

			// 收获地址
			if (StringUtils.isNotBlank(wayBill.getRecAddress())) {
				BoolQueryBuilder query = new BoolQueryBuilder();

				QueryBuilder wildcardQuery = new WildcardQueryBuilder("recAddress",
						"*" + wayBill.getRecAddress() + "*");
				query.should(wildcardQuery);

				QueryStringQueryBuilder stringQueryBuilder = new QueryStringQueryBuilder(wayBill.getRecAddress())
						.field("recAddress").defaultOperator(Operator.AND);
				query.should(stringQueryBuilder);

				boolQuery.must(query);
			}
			// sendProNum 产品类型
			if (StringUtils.isNotBlank(wayBill.getSendProNum())) {
				QueryBuilder termQueryr = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
				boolQuery.must(termQueryr);
			}
			// signStatus 运单状态
			if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
				QueryBuilder termQueryr = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
				boolQuery.must(termQueryr);
			}
			// 封装条件

			return wayBillIndexRepository.search(boolQuery, pageable);
		}

	}

	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}

}
