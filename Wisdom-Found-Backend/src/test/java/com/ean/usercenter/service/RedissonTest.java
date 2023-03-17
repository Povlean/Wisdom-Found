package com.ean.usercenter.service;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @description:TODO
 * @author:Povlean
 */
@SpringBootTest
public class RedissonTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    void test() {
        // list
        RList<Object> rList = redissonClient.getList("test-list");
        rList.add("Povlean");
        System.out.println("rList:" + rList.get(0));
        // rList.remove(0);
    }

}
