package com.neo.yhrpc.annotation;

import java.lang.annotation.*;

/**
 * @Author: ncjdjyh
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
