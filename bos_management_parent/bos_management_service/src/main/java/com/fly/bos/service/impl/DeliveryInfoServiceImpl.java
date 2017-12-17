package com.fly.bos.service.impl;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.dao.DeliveryInfoDao;
import com.fly.bos.dao.TransitInfoDao;
import com.fly.bos.domain.base.DeliveryInfo;
import com.fly.bos.domain.base.TransitInfo;
import com.fly.bos.service.DeliveryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("deliveryInfoService")
@Transactional
public class DeliveryInfoServiceImpl extends BaseServiceImpl<DeliveryInfo, Integer> implements DeliveryInfoService {
    @Autowired
    private DeliveryInfoDao deliveryInfoDao;
    @Autowired
    private TransitInfoDao transitInfoDao;
    @Override
    public BaseDao<DeliveryInfo, Integer> getBaseDao() {
        return deliveryInfoDao;
    }

    @Override
    public void save(DeliveryInfo deliveryInfo, Integer transitInfoId) {
        deliveryInfoDao.save(deliveryInfo);
        TransitInfo transitInfo = transitInfoDao.findOne(transitInfoId);
        transitInfo.setDeliveryInfo(deliveryInfo);
        transitInfo.setStatus("开始配送");
    }
}
