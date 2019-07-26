package com.neo.yhrpc.generate;

import com.neo.yhrpc.annotation.*;
import com.neo.yhrpc.common.ReflectMessageHandler;
import com.neo.yhrpc.consumer.RpcConsumer;
import com.neo.yhrpc.provider.RpcProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @Author: ncjdjyh
 * @FirstInitial: 2019/7/25
 * @Description: ~
 */
public class FeignServerRegister implements ImportBeanDefinitionRegistrar,
        ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware, BeanFactoryAware {
    private ClassLoader classLoader;
    private ResourceLoader resourceLoader;
    private Environment environment;
    private BeanFactory beanFactory;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        try {
            registerRpcProvider(importingClassMetadata, registry);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void registerRpcProvider(AnnotationMetadata metadata,
                                    BeanDefinitionRegistry registry) throws ClassNotFoundException {
        ClassPathScanningCandidateComponentProvider scanner = GenerateUtil.getScanner(this.environment);
        scanner.setResourceLoader(this.resourceLoader);
        String basePackage;
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(FeignServer.class);
        scanner.addIncludeFilter(annotationTypeFilter);
        basePackage = GenerateUtil.getBasePackage(metadata);
        // scan if there is FeignServer
        Set<BeanDefinition> beanDefinitionSet = scanner.findCandidateComponents(basePackage);
        RpcProvider provider = new RpcProvider("localhost", 8000);
        for (BeanDefinition beanDefinition : beanDefinitionSet) {
            if (beanDefinition instanceof AnnotatedBeanDefinition) {
                String className = beanDefinition.getBeanClassName();
                Class clazz = Class.forName(className);
                Method[] declaredMethods = clazz.getDeclaredMethods();
                for (Method method : declaredMethods) {
                    RpcResponse anno = AnnotationUtils.getAnnotation(method, RpcResponse.class);
                    if (Objects.nonNull(anno)) {
                        provider.serviceReflect(anno.value(), Object[].class, new ReflectMessageHandler(method, clazz , anno.value()));
                    }
                }
            }
        }
        provider.start();
    }
}
