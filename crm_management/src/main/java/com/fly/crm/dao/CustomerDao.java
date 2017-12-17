package com.fly.crm.dao;

import com.fly.bos.base.dao.BaseDao;
import com.fly.crm.domain.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerDao extends BaseDao<Customer, Integer> {
    List<Customer> findByFixedAreaId(String id);

    List<Customer> findByFixedAreaIdIsNull();

    @Query("update Customer set fixedAreaId=null where fixedAreaId=?1")
    @Modifying
    void updateFixedAreaIdToNull(String fixedAreaId);

    @Query("update Customer set fixedAreaId=?2 where id=?1")
    @Modifying
    void updateFixedAreaIdTo(Integer id, String fixedAreaId);

    Customer findByMobilePhone(String mobilePhone);

    Customer findByMobilePhoneAndPassword(String mobilePhone, String password);
}
