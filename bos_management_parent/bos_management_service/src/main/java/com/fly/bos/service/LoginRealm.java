package com.fly.bos.service;


import com.fly.bos.dao.PermissionDao;
import com.fly.bos.dao.RoleDao;
import com.fly.bos.dao.UserDao;
import com.fly.bos.domain.base.Permission;
import com.fly.bos.domain.base.Role;
import com.fly.bos.domain.base.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LoginRealm extends AuthorizingRealm {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RoleDao roleDao;
    /**
     * 获取授权信息
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        User user = (User)principalCollection.getPrimaryPrincipal();
        List<Permission> permissions = null;
        List<Role> roles = null;
        if ("admin".equals(user.getUsername())) {
            //管理员
            permissions = permissionDao.findAll();
            roles = roleDao.findAll();
        } else {
            //查询当前用户的权限和角色
            permissions = permissionDao.findPermissionsByUser(user.getId());
            roles = roleDao.findRolesByUser(user.getId());
        }


        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        for (Permission permission : permissions) {
            authorizationInfo.addStringPermission(permission.getKeyword());
        }
        for (Role role : roles) {
            authorizationInfo.addRole(role.getKeyword());
        }
        return authorizationInfo;
    }

    /**
     * 获取认证信息
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        User user = userDao.findByUsername(username);

        if (null != user) {
            return new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
        }

        return null;
    }
}
