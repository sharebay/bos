package com.fly.crm.service.impl;

import com.fly.bos.base.dao.BaseDao;
import com.fly.crm.dao.CustomerDao;
import com.fly.crm.domain.Customer;
import com.fly.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public void active(String mobilePhone) {
        Customer customer = customerDao.findByMobilePhone(mobilePhone);
        if (customer.getType() == null) {
            customer.setType(1);
        }
        customerDao.save(customer);
    }

    @Override
    public void signUp(Customer customer) {
        customerDao.save(customer);
    }

    @Override
    public List<Customer> findAll() {
        return customerDao.findAll();
    }

    @Override
    public List<Customer> findByFixedAreaId(String id) {

        if ("isNull".equals(id)) {
            return customerDao.findByFixedAreaIdIsNull();
        }

        return customerDao.findByFixedAreaId(id);
    }

    @Override
    public void associateCustomersWithFixedArea(String fixedAreaId, String customerIds) {
        List<Integer> ids = new ArrayList<>();
        if (!"null".equals(customerIds)) {
            String[] idStringArray = customerIds.split(",");
            if (idStringArray != null) {
                for (String id : idStringArray) {
                    ids.add(Integer.parseInt(id.trim()));
                }
            }
        }

        //将fixedAreaId清空
        customerDao.updateFixedAreaIdToNull(fixedAreaId);

        //给指定的customer update
        for (Integer id : ids) {
            customerDao.updateFixedAreaIdTo(id, fixedAreaId);
        }
    }

    @Override
    public Customer login(String mobilePhone, String password) {
        return customerDao.findByMobilePhoneAndPassword(mobilePhone, password);
    }

    @Override
    public String getFixedAreaId(Integer id) {
        Customer customer = customerDao.findOne(id);
        return customer.getFixedAreaId();
    }
}
