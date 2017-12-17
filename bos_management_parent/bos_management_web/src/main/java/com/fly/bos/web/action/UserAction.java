package com.fly.bos.web.action;

import com.fly.bos.domain.base.Role;
import com.fly.bos.domain.base.User;
import com.fly.bos.service.BaseService;
import com.fly.bos.service.UserService;
import com.fly.bos.util.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@Scope("prototype")
@Namespace("/user")
@ParentPackage("struts-default")
public class UserAction extends BaseAction<User, Integer> {
    @Autowired
    private UserService userService;
    @Override
    BaseService<User, Integer> getService() {
        return userService;
    }

    /**
     * 用户登陆
     * @return
     */
    @Action(value = "login", results = {
            @Result(name = "success", location = "/index.jsp", type = "redirect"),
            @Result(name = "fail", location = "/login.jsp", type = "redirect")
    })
    public String login() {
        String key = (String) ServletActionContext.getRequest().getSession().getAttribute("key");
        if (StringUtils.isNotBlank(checkcode) && checkcode.equals(key)) {
            Subject subject = SecurityUtils.getSubject();
            AuthenticationToken token = new UsernamePasswordToken(
                    getModel().getUsername(),
                    MD5Utils.md5(getModel().getPassword())
            );
            subject.login(token);
            return SUCCESS;
        }
        return "fail";
    }
    //属性驱动
    private String checkcode;

    public String getCheckcode() {
        return checkcode;
    }

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    /**
     * 用户注销
     * @return
     */
    @Action(value = "logout", results = {
            @Result(name = "success", location = "/login.jsp", type = "redirect")
    })
    public String logout() {

        SecurityUtils.getSubject().logout();

        return SUCCESS;
    }

    /**
     * 用户添加
     * @return
     * @throws IOException
     */
    @Override
    @Action("save")
    public String save() throws IOException {
        System.out.println(roleIds);
        if (StringUtils.isNotBlank(roleIds)) {
            String[] roleId = roleIds.split(",");
            for (String id : roleId) {
                Role role = new Role();
                role.setId(Integer.parseInt(id.trim()));
                getModel().getRoles().add(role);
            }
        }
        getModel().setPassword(MD5Utils.md5(getModel().getPassword()));
        return super.save();
    }
    //属性驱动 roleIds
    private String roleIds;

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }
}
