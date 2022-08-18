package com.neo.yhrpc.provider;

import com.neo.yhrpc.common.IMessageHandler;
import com.neo.yhrpc.common.MessageDecoder;
import com.neo.yhrpc.common.MessageEncoder;
import com.neo.yhrpc.common.RegisterCenter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
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
    private String serviceName;
    private ProviderMessageCollector collector;

    public RpcProvider(String ip, int port, String serviceName) {
        this.ip = ip;
        this.port = port;
        this.serviceName = serviceName;
        collector = new ProviderMessageCollector();
    }

    public RpcProvider service(String signature, Class<?> returnClass, IMessageHandler handler) {
        this.collector.register(signature, returnClass, handler);
        return this;
    }

    public void start() {
        RegisterCenter.register(this);
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(bossGroup, workerGroup)
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
            ChannelFuture c = bootstrap.bind(ip, port).syncUninterruptibly();
            System.out.println("service at " + ip + ":" + port);
            c.channel().closeFuture().syncUninterruptibly();
            System.out.println("service over");
        } finally {
            System.out.println("service over");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            RegisterCenter.deregister(this);
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
