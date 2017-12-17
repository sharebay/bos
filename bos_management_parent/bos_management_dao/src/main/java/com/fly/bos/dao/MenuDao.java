package com.fly.bos.dao;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.domain.base.Menu;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuDao extends BaseDao<Menu, Integer> {
    @Query("select m from Menu m join m.roles r join r.users u where u.id=?1 and m.parentMenu is null")
    List<Menu> findMenuByUser(Integer id);
}
