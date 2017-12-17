package com.fly.bos.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.service.BaseService;

public abstract class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {
	
	public abstract BaseDao<T, ID> getBaseDao();

	@Override
	public Page<T> findAll(Specification<T> spec, Pageable pageable) {
		return getBaseDao().findAll(spec, pageable);
	}
	
	@Override
	public Page<T> findAll(Pageable pageable) {
		return getBaseDao().findAll(pageable);
	}

	@Override
	public <S extends T> S save(S entity) {
		return getBaseDao().save(entity);
	}
	
	@Override
	public <S extends T> Iterable<S> save(Iterable<S> entities) {
		return getBaseDao().save(entities);
	}

	@Override
	public T findOne(ID id) {
		return getBaseDao().findOne(id);
	}

	@Override
	public boolean exists(ID id) {
		return getBaseDao().exists(id);
	}

	@Override
	public List<T> findAll() {
		return getBaseDao().findAll();
	}

	@Override
	public List<T> findAll(Iterable<ID> ids) {
		return getBaseDao().findAll(ids);
	}

	@Override
	public long count() {
		return getBaseDao().count();
	}

	@Override
	public void delete(ID id) {
		getBaseDao().delete(id);
	}

	@Override
	public void delete(T entity) {
		getBaseDao().delete(entity);
	}

	@Override
	public void delete(Iterable<ID> ids) {
		for (ID id : ids) {
			getBaseDao().delete(id);
		}
	}

	@Override
	public void deleteAll() {
		getBaseDao().deleteAll();
	}

}
