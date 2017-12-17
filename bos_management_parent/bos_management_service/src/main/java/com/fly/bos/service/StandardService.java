package com.fly.bos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fly.bos.domain.base.Standard;

public interface StandardService {

	/***********************************
	 * 添加Standard
	 * @param standard
	 ***********************************/
	void save(Standard standard);

	/***********************************
	 * 分页查询
	 * @param pageable
	 * @return
	 ***********************************/
	Page<Standard> queryPage(Pageable pageable);

	/***********************************
	 * 通过id删除standard
	 * @param parseInt
	 ***********************************/
	void deleteStandardById(int parseInt);

	/***********************************
	 * 查询所有标准
	 * @return
	 ***********************************/
	List<Standard> findAll();

}
