package com.fly.jedis.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class JedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test01() {
        Jedis jedis = new Jedis("192.168.233.200");
        Set<String> keys = jedis.keys("*");
        System.out.println(keys);

    }

    @Test
    public void test02() {
        redisTemplate.opsForValue().set("redisTemplate", "Hello World", 20, TimeUnit.SECONDS);

        Object s = this.redisTemplate.opsForValue().get("redisTemplate");
        System.out.println(s);
    }
}
