package com.neo.yhrpc.generate;

import java.lang.reflect.Method;

/**
 * @Author: neo
 * @FirstInitial: 2019/7/15
 * @Description: ~
 */
public interface RpcHandler {
    Object handle(Method method);
}
