package com.fly.bos.service.impl;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.dao.MenuDao;
import com.fly.bos.domain.base.Menu;
import com.fly.bos.domain.base.User;
import com.fly.bos.service.MenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service("menuService")
@Transactional
public class MenuServiceImpl extends BaseServiceImpl<Menu, Integer> implements MenuService {
    @Autowired
    private MenuDao menuDao;
    @Override
    public BaseDao<Menu, Integer> getBaseDao() {
        return menuDao;
    }

    @Override
    public List<Menu> showMenu() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if ("admin".equals(user.getUsername())) {
            List<Menu> all = menuDao.findAll();
            Iterator<Menu> iterator = all.iterator();
            while (iterator.hasNext()) {
                Menu next = iterator.next();
                if (next.getParentMenu() != null) {
                    iterator.remove();
                }
            }
            return all;
        }
        List<Menu> menus = menuDao.findMenuByUser(user.getId());
        return menus;
    }
}
