package com.neo.yhrpc.generate;

import com.neo.yhrpc.annotation.EnableFeignClients;
import com.neo.yhrpc.annotation.FeignClient;
import com.neo.yhrpc.annotation.RpcCall;
import com.neo.yhrpc.consumer.RpcConsumer;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;


public class FeignClientRegister implements ImportBeanDefinitionRegistrar,
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
        registerFeignClients(importingClassMetadata, registry);
    }

    public void registerFeignClients(AnnotationMetadata metadata,
                                     BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = GenerateUtil.getScanner(this.environment);
        scanner.setResourceLoader(this.resourceLoader);
        String basePackage;
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(FeignClient.class);
        scanner.addIncludeFilter(annotationTypeFilter);
        basePackage = GenerateUtil.getBasePackage(metadata);
        // scan if there is FeignClient
        Set<BeanDefinition> beanDefinitionSet = scanner.findCandidateComponents(basePackage);
        for (BeanDefinition beanDefinition : beanDefinitionSet) {
            if (beanDefinition instanceof AnnotatedBeanDefinition) {
                registerBeans(((AnnotatedBeanDefinition) beanDefinition));
            }
        }
    }

    private void registerBeans(AnnotatedBeanDefinition beanDefinition) {
        String className = beanDefinition.getBeanClassName();
        ((DefaultListableBeanFactory) this.beanFactory).registerSingleton(className, createProxy(beanDefinition));
    }

    /**
     * create dynamic proxy instance
     * @param annotatedBeanDefinition
     * @return
     */
    private Object createProxy(AnnotatedBeanDefinition annotatedBeanDefinition) {
        try {
            AnnotationMetadata m = annotatedBeanDefinition.getMetadata();
            Map<String, Object> attr = m.getAnnotationAttributes(FeignClient.class.getName());
            String ip = (String) attr.get("ip");
            int port = (int) attr.get("port");
            AnnotationMetadata annotationMetadata = annotatedBeanDefinition.getMetadata();
            Class<?> target = Class.forName(annotationMetadata.getClassName());
            return Proxy.newProxyInstance(target.getClassLoader(),
                    new Class[]{target},
                    new RpcInvokeHandler(ip, port));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class RpcInvokeHandler implements InvocationHandler {
        private String ip;
        private int port;
        private RpcConsumer consumer;

        public RpcInvokeHandler(String ip, int port) {
            this.ip = ip;
            this.port = port;
            consumer = new RpcConsumer(ip, port);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
            RpcCall rpcCall = AnnotationUtils.getAnnotation(method, RpcCall.class);
            if (rpcCall != null) {
                String signature = rpcCall.value();
                consumer.rpc(signature, method.getReturnType());
                return consumer.send(signature, args);
            }
            return method.invoke(proxy, args);
        }
    }
}