package com.fly.bos.service.impl;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.dao.SignInfoDao;
import com.fly.bos.dao.TransitInfoDao;
import com.fly.bos.domain.base.SignInfo;
import com.fly.bos.domain.base.TransitInfo;
import com.fly.bos.service.SignInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("signInfoService")
@Transactional
public class SignInfoServiceImpl extends BaseServiceImpl<SignInfo, Integer> implements SignInfoService {
    @Autowired
    private SignInfoDao signInfoDao;
    @Autowired
    private TransitInfoDao transitInfoDao;
    @Override
    public BaseDao<SignInfo, Integer> getBaseDao() {
        return signInfoDao;
    }

    @Override
    public void save(SignInfo signInfo, Integer transitInfoId) {
        signInfoDao.save(signInfo);
        TransitInfo transitInfo = transitInfoDao.findOne(transitInfoId);
        transitInfo.setSignInfo(signInfo);
        transitInfo.setStatus("签收录入");
    }
}
