package com.fly.bos.dao;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.domain.base.Role;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleDao extends BaseDao<Role, Integer> {
    @Query("select r from Role r join r.users u where u.id = ?1")
    List<Role> findRolesByUser(Integer id);
}
