package com.neo.yhrpc.common;

import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: ncjdjyh
 * @FirstInitial: 2019/7/25
 * @Description: ~
 */
public class ReflectMessageHandler {
    private Method method;
    private Class<?> clazz;
    private String signature;

    public ReflectMessageHandler(Method method, Class<?> clazz, String signature) {
        this.signature = signature;
        this.method = method;
        this.clazz = clazz;
    }

    public void handle(ChannelHandlerContext ctx, String requestId, Object[] args) {
        try {
            Object ret = method.invoke(clazz.newInstance(), args);
            ctx.writeAndFlush(new MessageOutput(requestId, signature, ret));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
