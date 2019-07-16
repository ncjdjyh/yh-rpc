package com.neo.yhrpc;

import com.neo.yhrpc.annotation.EnableFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: neo
 * @FirstInitial: 2019/7/14
 * @Description: ~
 */
@EnableFeignClients
@SpringBootApplication
public class RegisterApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegisterApplication.class);
    }
}
