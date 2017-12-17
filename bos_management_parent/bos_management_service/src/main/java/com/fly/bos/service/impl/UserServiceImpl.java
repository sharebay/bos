package com.fly.bos.service.impl;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.dao.UserDao;
import com.fly.bos.domain.base.User;
import com.fly.bos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User, Integer> implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public BaseDao<User, Integer> getBaseDao() {
        return userDao;
    }
}
