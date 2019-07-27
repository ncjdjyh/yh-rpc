package com.neo.yhrpc.provider;

import com.neo.yhrpc.common.IMessageHandler;
import com.neo.yhrpc.common.MessageDecoder;
import com.neo.yhrpc.common.MessageEncoder;
import com.neo.yhrpc.common.ReflectMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * @Author: neo
 * @FirstInitial: 2019/7/13
 * @Description: ~
 */
public class RpcProvider {
    private String ip;
    private int port;
    private ProviderMessageCollector collector;

    public RpcProvider(String ip, int port) {
        this.ip = ip;
        this.port = port;
        collector = new ProviderMessageCollector();
    }

    public RpcProvider service(String signature, Class<?> returnClass, IMessageHandler handler) {
        this.collector.register(signature, returnClass, handler);
        return this;
    }

    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(io.netty.channel.socket.SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new MessageEncoder());
                        p.addLast(new MessageDecoder());
                        p.addLast(collector);
                    }
                });
        bootstrap.bind(ip, port).syncUninterruptibly();
        System.out.println("service at " + ip + ":" + port);
    }

    public void stop() {

    }
}
