package com.neo.yhrpc.provider;

import com.neo.yhrpc.common.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: neo
 * @FirstInitial: 2019/7/13
 * @Description: ~
 */
public class ProviderMessageCollector extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext context;
    private ExecutorService executor;
    private MessageHandlers handlers = new MessageHandlers();
    private MessageRegistry registry = new MessageRegistry();

    {
        handlers.defaultHandler(new DefaultHandler());
    }

    public ProviderMessageCollector() {
        ThreadFactory factory = new ThreadFactory() {
            AtomicInteger seq = new AtomicInteger();
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("rpc-" + seq.getAndIncrement());
                return t;
            }

        };
        this.executor = Executors.newCachedThreadPool(factory);
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof MessageInput) {
            this.executor.execute(() -> handleMessage(ctx, (MessageInput) msg));
        }
    }

    private void handleMessage(ChannelHandlerContext ctx, MessageInput input) {
        Class<?> clazz = registry.get(input.getType());
        if (clazz == null) {
            handlers.defaultHandler().handle(ctx, input.getRequestId(), input);
            return;
        }
        Object o = input.getPayload(clazz);
        IMessageHandler<Object> handler = (IMessageHandler<Object>) handlers.get(input.getType());
        if (handler != null) {
            handler.handle(ctx, input.getRequestId(), o);
        } else {
            handlers.defaultHandler().handle(ctx, input.getRequestId(), input);
        }
    }

    public void register(String signature, Class<?> returnClass, IMessageHandler handler) {
        this.handlers.register(signature, handler);
        this.registry.register(signature, returnClass);
    }

}
