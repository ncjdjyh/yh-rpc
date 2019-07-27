package com.neo.yhrpc.annotation;

import com.neo.yhrpc.generate.FeignClientRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author: ncjdjyh
 * @FirstInitial: 2019/7/14
 * @Description: ~
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FeignClientRegister.class)
public @interface EnableFeignClients {
    String[] basePackages() default {};
    Class<?>[] clients() default {};
}
