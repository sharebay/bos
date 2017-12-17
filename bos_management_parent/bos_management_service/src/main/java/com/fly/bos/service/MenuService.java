package com.fly.bos.service;

import com.fly.bos.domain.base.Menu;

import java.util.List;

public interface MenuService extends BaseService<Menu, Integer> {
    List<Menu> showMenu();
}
