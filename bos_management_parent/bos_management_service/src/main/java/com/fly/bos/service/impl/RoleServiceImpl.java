package com.fly.bos.service.impl;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.dao.RoleDao;
import com.fly.bos.domain.base.Role;
import com.fly.bos.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("roleService")
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role, Integer> implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Override
    public BaseDao<Role, Integer> getBaseDao() {
        return roleDao;
    }
}
