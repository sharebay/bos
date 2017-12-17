package com.fly.bos.service.impl;

import com.fly.bos.domain.base.FixedArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.dao.SubAreaDao;
import com.fly.bos.domain.base.SubArea;
import com.fly.bos.service.SubAreaService;

import java.util.Set;

@Service("subAreaService")
@Transactional
public class SubAreaServiceImpl extends BaseServiceImpl<SubArea, String> implements SubAreaService {

	@Autowired
	private SubAreaDao subAreaDao;
	
	@Override
	public BaseDao<SubArea, String> getBaseDao() {
		return subAreaDao;
	}


	@Override
	public Set<SubArea> findByFixedAreaIsNull() {
		return subAreaDao.findByFixedAreaIsNull();
	}

	@Override
	public Set<SubArea> findByFixedArea(FixedArea fixedArea) {
		return subAreaDao.findByFixedArea(fixedArea);
	}
}
