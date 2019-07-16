package com.neo.yhrpc.demo;

import com.neo.yhrpc.annotation.FeignClient;
import com.neo.yhrpc.annotation.RpcCall;
import org.springframework.stereotype.Component;

/**
 * @Author: neo
 * @FirstInitial: 2019/7/14
 * @Description: ~
 */
@FeignClient(ip = "localhost", port = 8000)
@Component
public interface DemoFeignClient {
    @RpcCall("fib")
    Long sum(int n);
}
