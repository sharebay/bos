package com.fly.bos.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fly.bos.domain.base.Menu;
import com.fly.bos.service.BaseService;
import com.fly.bos.service.MenuService;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("prototype")
@Namespace("/menu")
@ParentPackage("struts-default")
public class MenuAction extends BaseAction<Menu, Integer> {
    @Autowired
    private MenuService menuService;
    @Override
    BaseService<Menu, Integer> getService() {
        return menuService;
    }

    @Override
    protected Specification<Menu> getSpecification() {
        Specification<Menu> specification = new Specification<Menu>() {
            @Override
            public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.isNull(root.get("parentMenu").as(Menu.class));
                return predicate;
            }
        };
        return specification;
    }


    @Action("findAllForTree")
    public String findAllForTree() throws IOException {
        Pageable pageable = new PageRequest(0, 50);
        Page<Menu> page = getService().findAll(getSpecification(), pageable);

        String jsonString = JSON.toJSONString(page.getContent(), SerializerFeature.DisableCircularReferenceDetect);

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
        response.getWriter().write(jsonString);
        return NONE;
    }

    @Override
    @Action("save")
    public String save() throws IOException {
        if (getModel().getParentMenu() != null
                && getModel().getParentMenu().getId() == null) {
            getModel().setParentMenu(null);
        }
        return super.save();
    }

    /**
     * 页面展示的menu
     * @return
     */
    @Action("showMenu")
    public String showMenu() throws IOException {
        List<Menu> list = menuService.showMenu();
        writeJson(JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect));
        return NONE;
    }

}
