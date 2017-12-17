package com.fly.bos.service.impl;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.dao.PermissionDao;
import com.fly.bos.domain.base.Permission;
import com.fly.bos.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("permissionService")
@Transactional
public class PermissionServiceImpl extends BaseServiceImpl<Permission, Integer> implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Override
    public BaseDao<Permission, Integer> getBaseDao() {
        return permissionDao;
    }
}
