package com.neo.yhrpc.consumer;

import com.neo.yhrpc.common.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ncjdjyh
 * @FirstInitial: 2019/7/13
 * @Description: ~
 */
public class RpcConsumer {
    private String ip;
    private int port;
    private EventLoopGroup group;
    private ConsumerMessageCollector collector;
    private Bootstrap bootstrap;
    private boolean started;
    private boolean stopped;

    public RpcConsumer(String host, int port) {
        this.ip = host;
        this.port = port;
        init();
    }

    public RpcConsumer rpc(String signature, Class<?> returnClass) {
        this.collector.register(signature, returnClass);
        return this;
    }

    public <T> RpcFuture<T> sendAsync(String type, Object payload) {
        if (!started) {
            connect();
            started = true;
        }
        String requestId = RequestId.next();
        MessageOutput output = new MessageOutput(requestId, type, payload);
        return collector.send(output);
    }

    public <T> T send(String signature, Object payLoad) {
        RpcFuture<T> future = sendAsync(signature, payLoad);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RPCException(e);
        }
    }

    private void init() {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup(1);
        bootstrap.group(group);
        collector = new ConsumerMessageCollector(new MessageRegistry());
        bootstrap.channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipe = ch.pipeline();
                pipe.addLast(new ReadTimeoutHandler(60));
                pipe.addLast(new MessageDecoder());
                pipe.addLast(new MessageEncoder());
                pipe.addLast(collector);
            }

        });
        bootstrap.option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true);
    }

    private void connect() {
        bootstrap.connect(ip, port).syncUninterruptibly();
    }

    public void close() {
        stopped = true;
        collector.close();
        group.shutdownGracefully(0, 5000, TimeUnit.SECONDS);
    }
}
