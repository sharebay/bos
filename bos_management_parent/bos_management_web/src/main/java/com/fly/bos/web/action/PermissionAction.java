package com.fly.bos.web.action;

import com.fly.bos.domain.base.Permission;
import com.fly.bos.service.BaseService;
import com.fly.bos.service.PermissionService;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
@Namespace("/permission")
@ParentPackage("struts-default")
public class PermissionAction extends BaseAction<Permission, Integer> {
    @Autowired
    private PermissionService permissionService;
    @Override
    BaseService<Permission, Integer> getService() {
        return permissionService;
    }
}
