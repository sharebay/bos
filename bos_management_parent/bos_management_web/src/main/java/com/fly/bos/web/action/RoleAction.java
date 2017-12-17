package com.fly.bos.web.action;

import com.fly.bos.domain.base.Menu;
import com.fly.bos.domain.base.Permission;
import com.fly.bos.domain.base.Role;
import com.fly.bos.service.BaseService;
import com.fly.bos.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/role")
public class RoleAction extends BaseAction<Role, Integer> {
    @Autowired
    private RoleService roleService;
    @Override
    BaseService<Role, Integer> getService() {
        return roleService;
    }

    /**
     * 重写保存方法
     * @return
     * @throws IOException
     */
    @Override
    @Action("save")
    public String save() throws IOException {
        //解析数据
        if (StringUtils.isNotBlank(permissionIds)) {
            String[] permissions = permissionIds.split(", ");
            for (String permissionId : permissions) {
                Permission permission = new Permission();
                permission.setId(Integer.parseInt(permissionId.trim()));
                getModel().getPermissions().add(permission);
            }
        }
        if (StringUtils.isNotBlank(menuIds)) {
            String[] menus = menuIds.trim().split(" ");
            for (String menuId : menus) {
                Menu menu = new Menu();
                menu.setId(Integer.parseInt(menuId.trim()));
                getModel().getMenus().add(menu);
            }
        }

        return super.save();
    }
    //属性驱动
    private String permissionIds;
    private String menuIds;

    public String getPermissionIds() {
        return permissionIds;
    }
    public void setPermissionIds(String permissionIds) {
        this.permissionIds = permissionIds;
    }
    public String getMenuIds() {
        return menuIds;
    }
    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }
}
