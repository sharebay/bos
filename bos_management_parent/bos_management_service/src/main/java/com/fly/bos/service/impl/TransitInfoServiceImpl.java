package com.fly.bos.service.impl;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.dao.TransitInfoDao;
import com.fly.bos.dao.WayBillDao;
import com.fly.bos.domain.base.TransitInfo;
import com.fly.bos.domain.base.WayBill;
import com.fly.bos.index.WayBillIndexDao;
import com.fly.bos.service.TransitInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("transitInfoService")
@Transactional
public class TransitInfoServiceImpl extends BaseServiceImpl<TransitInfo, Integer> implements TransitInfoService {

    @Autowired
    private TransitInfoDao transitInfoDao;
    @Autowired
    private WayBillDao wayBillDao;
    @Autowired
    private WayBillIndexDao wayBillIndexDao;

    @Override
    public BaseDao<TransitInfo, Integer> getBaseDao() {
        return transitInfoDao;
    }

    @Override
    public void beginTransit(List<Integer> ids) {
        for (Integer id : ids) {
            WayBill wayBill = wayBillDao.findOne(Long.valueOf(id));
            if (1 == wayBill.getSignStatus()) {
                wayBill.setSignStatus(2);
                wayBillIndexDao.save(wayBill);
                TransitInfo transitInfo = new TransitInfo();
                transitInfo.setStatus("出入库中转");
                transitInfo.setWayBill(wayBill);
                transitInfoDao.save(transitInfo);
            }
        }
    }
}
