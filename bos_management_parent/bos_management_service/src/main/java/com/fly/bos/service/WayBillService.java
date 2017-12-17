package com.fly.bos.service;

import com.fly.bos.domain.base.WayBill;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WayBillService extends BaseService<WayBill, Long> {
    Page<WayBill> findAll(QueryBuilder queryBuilder, Pageable pageable);
}
