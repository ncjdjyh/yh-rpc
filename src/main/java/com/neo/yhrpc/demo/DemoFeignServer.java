package com.neo.yhrpc.demo;

import com.neo.yhrpc.annotation.FeignServer;
import com.neo.yhrpc.annotation.RpcResponse;
import org.springframework.stereotype.Component;

/**
 * @Author: ncjdjyh
 * @FirstInitial: 2019/7/25
 * @Description: ~
 */
@FeignServer
@Component
public class DemoFeignServer {
    @RpcResponse("sum")
    public int sum(int a, int b) {
       return a + b;
    }
}
