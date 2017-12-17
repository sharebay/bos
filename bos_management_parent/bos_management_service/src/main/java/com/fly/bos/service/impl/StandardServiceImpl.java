package com.fly.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fly.bos.dao.StandardDao;
import com.fly.bos.domain.base.Standard;
import com.fly.bos.service.StandardService;

@Service("standardService")
@Transactional
public class StandardServiceImpl implements StandardService {

	@Autowired
	private StandardDao standardDao;
	
	@Override
	public void save(Standard standard) {
		standardDao.save(standard);
	}

	@Override
	public Page<Standard> queryPage(Pageable pageable) {
		return standardDao.findAll(pageable);
	}

	@Override
	public void deleteStandardById(int id) {
		standardDao.delete(id);
	}

	@Override
	public List<Standard> findAll() {
		return standardDao.findAll();
	}
	
}
