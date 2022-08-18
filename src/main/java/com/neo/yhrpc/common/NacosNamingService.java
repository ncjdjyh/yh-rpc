package com.neo.yhrpc.common;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: ncjdjyh
 * @since: 2022/8/18
 */
public class NacosNamingService {
    private static NamingService instance;

    private NacosNamingService() {
    }

    public static synchronized NamingService getInstance() throws NacosException {
        if (instance == null) {
            instance = NamingFactory.createNamingService("127.0.0.1:8848");
        }
        return instance;
    }
}
