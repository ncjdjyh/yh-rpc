package com.neo.yhrpc.generate;

import com.neo.yhrpc.annotation.EnableFeignClients;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Author: ncjdjyh
 * @FirstInitial: 2019/7/25
 * @Description: ~
 */
public class GenerateUtil {
    public static ClassPathScanningCandidateComponentProvider getScanner(Environment environment) {
        return new ClassPathScanningCandidateComponentProvider(false, environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

    public static String getBasePackage(AnnotationMetadata importingClassMetadata) {

        String basePackage = null;

        if (basePackage == null) {
            basePackage = ClassUtils.getPackageName(importingClassMetadata.getClassName());
        }

        return basePackage;
    }
}
