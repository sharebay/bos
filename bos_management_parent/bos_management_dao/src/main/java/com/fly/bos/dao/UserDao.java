package com.fly.bos.dao;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.domain.base.Permission;
import com.fly.bos.domain.base.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDao extends BaseDao<User, Integer> {
    User findByUsername(String username);

}
