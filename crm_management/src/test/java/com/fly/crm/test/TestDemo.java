package com.fly.crm.test;

import com.fly.crm.domain.Customer;
import com.fly.crm.service.CustomerService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class TestDemo {

    @Test
    public void test01() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        CustomerService customerService = (CustomerService) context.getBean("customerService");
        List<Customer> list = customerService.findAll();
        for (Customer customer : list) {
            System.out.println(customer.getAddress());
        }
    }
}
