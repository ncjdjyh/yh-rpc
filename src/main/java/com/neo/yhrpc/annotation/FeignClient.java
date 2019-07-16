package com.neo.yhrpc.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @Author: neo
 * @FirstInitial: 2019/7/14
 * @Description: ~
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FeignClient {
    String ip() ;
    int port();
}
