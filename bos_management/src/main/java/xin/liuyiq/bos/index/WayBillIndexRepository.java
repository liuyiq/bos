package xin.liuyiq.bos.index;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import xin.liuyiq.bos.domain.take_delivery.WayBill;

public interface WayBillIndexRepository extends ElasticsearchRepository<WayBill, Integer>{

}
