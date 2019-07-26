package com.neo.yhrpc.annotation;

import com.neo.yhrpc.generate.FeignServerRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author: ncjdjyh
 * @FirstInitial: 2019/7/25
 * @Description: ~
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FeignServerRegister.class)
public @interface EnableFeignServer {
    String[] basePackages() default {};
    Class<?>[] clients() default {};
}
