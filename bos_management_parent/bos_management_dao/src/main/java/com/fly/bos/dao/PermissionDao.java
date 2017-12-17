package com.fly.bos.dao;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.domain.base.Permission;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PermissionDao extends BaseDao<Permission, Integer> {
    @Query("select p from Permission p join p.roles r join r.users u where u.id=?1")
    List<Permission> findPermissionsByUser(Integer id);
}
