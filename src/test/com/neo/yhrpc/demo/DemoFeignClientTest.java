package com.neo.yhrpc.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author: neo
 * @FirstInitial: 2019/7/15
 * @Description: ~
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoFeignClientTest {
    @Autowired
    private DemoFeignClient feignClient;

    @Test
    public void sum() {
        Long a = feignClient.sum(1);
        System.out.println(a);
    }
}