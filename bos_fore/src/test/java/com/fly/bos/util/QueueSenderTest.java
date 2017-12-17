package com.fly.bos.util;

import org.apache.shiro.crypto.hash.Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class QueueSenderTest {

    @Autowired
    QueueSender queueSender;

    @Test
    public void send() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("phoneNumbers", "18866666666");
        map.put("code", "2342");
        queueSender.send("sms", map);
    }

    @Test
    public void send1() throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("Test", "Hello World 2");
        queueSender.send("firstQueue", map);
    }

}