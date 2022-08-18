package com.neo.yhrpc.common;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.neo.yhrpc.provider.RpcProvider;

/**
 * @author: ncjdjyh
 * @since: 2022/8/18
 */
public class RegisterCenter {
    private static final String serverList = "127.0.0.1:8848";

    private RegisterCenter() {
    }

    public static synchronized NamingService createNamingService() {
        try {
            return NamingFactory.createNamingService(serverList);
        } catch (NacosException e) {
            throw new RPCException("can not get nacos namingService", e);
        }
    }

    public static void register(RpcProvider rpcProvider) {
        try {
            createNamingService().registerInstance(rpcProvider.getServiceName(), rpcProvider.getIp(), rpcProvider.getPort());
        } catch (NacosException e) {
            throw new RPCException("register rpcProvider exception!", e);
        }
    }

    public static void deregister(RpcProvider rpcProvider) {
        try {
            createNamingService().deregisterInstance(rpcProvider.getServiceName(), rpcProvider.getIp(), rpcProvider.getPort());
        } catch (NacosException e) {
            throw new RPCException("deregister rpcProvider exception!", e);
        }
    }

    public static Instance getInstance(String serviceName) {
        try {
            return createNamingService().selectOneHealthyInstance(serviceName);
        } catch (NacosException e) {
            throw new RPCException("get service instance exception!", e);
        }
    }
}
