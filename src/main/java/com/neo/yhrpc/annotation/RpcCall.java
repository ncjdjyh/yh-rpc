package com.neo.yhrpc.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @Author: neo
 * @FirstInitial: 2019/7/14
 * @Description: ~
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcCall {
    String value();
}
