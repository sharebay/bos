package com.fly.bos.service.impl;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.dao.WayBillDao;
import com.fly.bos.domain.base.WayBill;
import com.fly.bos.index.WayBillIndexDao;
import com.fly.bos.service.WayBillService;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("wayBillService")
@Transactional
public class WayBillServiceImpl extends BaseServiceImpl<WayBill, Long> implements WayBillService {

    @Autowired
    private WayBillDao wayBillDao;
    @Autowired
    private WayBillIndexDao wayBillIndexDao;

    @Override
    public BaseDao<WayBill, Long> getBaseDao() {
        return wayBillDao;
    }

    @Override
    public <S extends WayBill> S save(S entity) {
        wayBillIndexDao.save(entity);
        S s = wayBillDao.save(entity);
        return s;
    }

    @Override
    public Page<WayBill> findAll(QueryBuilder queryBuilder, Pageable pageable) {
        return wayBillIndexDao.search(queryBuilder, pageable);
    }
}
