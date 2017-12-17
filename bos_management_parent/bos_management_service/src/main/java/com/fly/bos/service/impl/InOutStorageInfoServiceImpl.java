package com.fly.bos.service.impl;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.dao.InOutStorageInfoDao;
import com.fly.bos.dao.TransitInfoDao;
import com.fly.bos.domain.base.InOutStorageInfo;
import com.fly.bos.domain.base.TransitInfo;
import com.fly.bos.service.InOutStorageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("inOutStorageInfoService")
@Transactional
public class InOutStorageInfoServiceImpl extends BaseServiceImpl<InOutStorageInfo, Integer> implements InOutStorageInfoService {
    @Autowired
    private InOutStorageInfoDao inOutStorageInfoDao;

    @Autowired
    private TransitInfoDao transitInfoDao;

    @Override
    public BaseDao<InOutStorageInfo, Integer> getBaseDao() {
        return inOutStorageInfoDao;
    }

    @Override
    public void save(InOutStorageInfo inOutStorageInfo, Integer transitInfoId) {
        inOutStorageInfoDao.save(inOutStorageInfo);
        TransitInfo transitInfo = transitInfoDao.findOne(transitInfoId);
        transitInfo.getInOutStorageInfos().add(inOutStorageInfo);
        if ("到达网点".equals(inOutStorageInfo.getOperation())) {
            transitInfo.setStatus("到达网点");
            transitInfo.setOutletAddress(inOutStorageInfo.getAddress());
        }
    }
}
