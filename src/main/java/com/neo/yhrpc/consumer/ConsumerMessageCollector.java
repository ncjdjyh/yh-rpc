package com.neo.yhrpc.consumer;

import com.neo.yhrpc.common.MessageInput;
import com.neo.yhrpc.common.MessageOutput;
import com.neo.yhrpc.common.MessageRegistry;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: ncjdjyh
 * @FirstInitial: 2019/7/13
 * @Description: ~
 */
public class ConsumerMessageCollector extends ChannelInboundHandlerAdapter {
    private ChannelHandlerContext context;
    private ConcurrentMap<String, RpcFuture<?>> pendingTasks = new ConcurrentHashMap<>();
    private MessageRegistry registry;
    private Throwable ConnectionClosed = new Exception("rpc connection not active error");

    public ConsumerMessageCollector(MessageRegistry registry) {
        this.registry = registry;
    }

    public void register(String signature, Class<?> clazz) {
        this.registry.register(signature, clazz);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof MessageInput)) {
            return;
        }
        MessageInput input = (MessageInput) msg;
        Class<?> clazz = registry.get(input.getType());
        if (clazz == null) {
            return;
        }
        Object o = input.getPayload(clazz);
        RpcFuture<Object> future = (RpcFuture<Object>) pendingTasks.remove(input.getRequestId());
        if (future == null) {
            return;
        }
        future.success(o);
    }

    public void close() {
        ChannelHandlerContext ctx = context;
        if (ctx != null) {
            ctx.close();
        }
    }

    public <T> RpcFuture<T> send(MessageOutput output) {
        ChannelHandlerContext ctx = context;
        RpcFuture<T> future = new RpcFuture<>();
        if (ctx != null) {
            ctx.channel().eventLoop().execute(() -> {
                pendingTasks.put(output.getRequestId(), future);
                ctx.writeAndFlush(output);
            });
        } else {
            future.fail(ConnectionClosed);
        }
        return future;
    }
}
