package com.fly.bos.service.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.dao.CourierDao;
import com.fly.bos.domain.base.Courier;
import com.fly.bos.service.CourierService;

@Service("courierService")
@Transactional
public class CourierServiceImpl extends BaseServiceImpl<Courier, Integer> implements CourierService {

	@Autowired
	private CourierDao courierDao;
	
	@Override
	public BaseDao<Courier, Integer> getBaseDao() {
		return courierDao;
	}

	@Override
	public void deleteLogically(List<Integer> ids) {
        for (Integer id : ids) {
			courierDao.deleteLogically(id);
		}
	}

	@Override
	@RequiresPermissions("courier:restore")
	public void restore(List<Integer> ids) {
		for (Integer id : ids) {
			courierDao.restore(id);
		}
	}

}
