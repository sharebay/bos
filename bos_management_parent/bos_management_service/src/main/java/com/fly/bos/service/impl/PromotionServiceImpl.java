package com.fly.bos.service.impl;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.dao.PromotionDao;
import com.fly.bos.domain.base.Promotion;
import com.fly.bos.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("promotionService")
@Transactional
public class PromotionServiceImpl extends BaseServiceImpl<Promotion, Integer> implements PromotionService {
    @Autowired
    private PromotionDao promotionDao;
    @Override
    public BaseDao<Promotion, Integer> getBaseDao() {
        return promotionDao;
    }
}
