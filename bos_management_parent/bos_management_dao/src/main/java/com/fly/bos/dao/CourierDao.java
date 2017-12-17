package com.fly.bos.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.domain.base.Courier;

public interface CourierDao extends BaseDao<Courier, Integer> {

	/***********************************
	 * 逻辑删除
	 * @param id
	 ***********************************/
	@Query("update Courier set deltag='1' where id=?1")
	@Modifying
	void deleteLogically(Integer id);
	
	/***********************************
	 * 还原
	 * @param id
	 ***********************************/
	@Query("update Courier set deltag='0' where id=?1")
	@Modifying
	void restore(Integer id);

}
