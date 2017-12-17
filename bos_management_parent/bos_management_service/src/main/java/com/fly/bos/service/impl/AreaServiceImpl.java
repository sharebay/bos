package com.fly.bos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.dao.AreaDao;
import com.fly.bos.domain.base.Area;
import com.fly.bos.service.AreaService;

import java.util.List;

@Service("areaService")
@Transactional
public class AreaServiceImpl extends BaseServiceImpl<Area, String> implements AreaService {
	
	@Autowired
	private AreaDao areaDao;

	@Override
	public BaseDao<Area, String> getBaseDao() {
		return areaDao;
	}

	@Override
	public List<Area> findByShortcode(String shortcode) {
		return areaDao.findByShortcode(shortcode);
	}
}
