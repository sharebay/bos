package com.fly.bos.service.impl;

import com.fly.bos.dao.CourierDao;
import com.fly.bos.dao.TakeTimeDao;
import com.fly.bos.domain.base.Courier;
import com.fly.bos.domain.base.TakeTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.dao.FixedAreaDao;
import com.fly.bos.domain.base.FixedArea;
import com.fly.bos.service.FixedAreaService;

@Service("fixedAreaService")
@Transactional
public class FixedAreaServiceImpl extends BaseServiceImpl<FixedArea, String> implements FixedAreaService {

	@Autowired
	private FixedAreaDao fixedAreaDao;
	@Autowired
    private CourierDao courierDao;
	@Autowired
    private TakeTimeDao takeTimeDao;
	
	@Override
	public BaseDao<FixedArea, String> getBaseDao() {
		return fixedAreaDao;
	}

	@Override
	public void associateCourier(String id, Integer courierId, Integer takeTimeId) {
	    //查出快递员
        Courier courier = courierDao.findOne(courierId);
        //查出取派时间
        TakeTime takeTime = takeTimeDao.findOne(takeTimeId);
        //关联快递员和取派时间
        courier.setTakeTime(takeTime);
        //查询定区
        FixedArea fixedArea = fixedAreaDao.findOne(id);
        //关联定区和快递员
        fixedArea.getCouriers().add(courier);
        //更新
        fixedAreaDao.save(fixedArea);

    }
}
