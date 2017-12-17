package com.fly.bos.index;

import com.fly.bos.domain.base.WayBill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WayBillIndexDao extends ElasticsearchRepository<WayBill, Long> {
}
