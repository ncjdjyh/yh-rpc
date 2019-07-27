package com.neo.yhrpc;

import com.neo.yhrpc.annotation.EnableFeignClients;
import com.neo.yhrpc.annotation.EnableFeignServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: ncjdjyh
 * @FirstInitial: 2019/7/14
 * @Description: ~
 */
@EnableFeignServer
@EnableFeignClients
@SpringBootApplication
public class RegisterApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegisterApplication.class);
    }
}
