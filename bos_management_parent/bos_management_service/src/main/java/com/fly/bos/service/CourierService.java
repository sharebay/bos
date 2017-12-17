package com.fly.bos.service;

import java.util.List;

import com.fly.bos.domain.base.Courier;
import org.apache.shiro.authz.annotation.RequiresPermissions;

public interface CourierService extends BaseService<Courier, Integer> {

	/***********************************
	 * 逻辑删除list中的id对象
	 * @param ids
	 ***********************************/
	void deleteLogically(List<Integer> ids);

	
	/***********************************
	 * 还原
	 ***********************************/
	void restore(List<Integer> ids);

}
