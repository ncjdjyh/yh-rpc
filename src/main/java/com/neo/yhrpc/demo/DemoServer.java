package com.neo.yhrpc.demo;

import com.neo.yhrpc.common.IMessageHandler;
import com.neo.yhrpc.common.MessageOutput;
import com.neo.yhrpc.provider.RpcProvider;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class FibRequestHandler implements IMessageHandler<Integer> {

    private List<Long> fibs = new ArrayList<>();

    {
        fibs.add(1L); // fib(0) = 1
        fibs.add(1L); // fib(1) = 1
    }

    @Override
    public void handle(ChannelHandlerContext ctx, String requestId, Integer n) {
        for (int i = fibs.size(); i < n + 1; i++) {
            long value = fibs.get(i - 2) + fibs.get(i - 1);
            fibs.add(value);
        }
        ctx.writeAndFlush(new MessageOutput(requestId, "fib", fibs.get(n)));
    }

}

class ExpRequestHandler implements IMessageHandler<ExpRequest> {

    @Override
    public void handle(ChannelHandlerContext ctx, String requestId, ExpRequest message) {
        int base = message.getBase();
        int exp = message.getExp();
        long start = System.nanoTime();
        long res = 1;
        for (int i = 0; i < exp; i++) {
            res *= base;
        }
        long cost = System.nanoTime() - start;
        ctx.writeAndFlush(new MessageOutput(requestId, "exp", new ExpResponse(res, cost)));
    }

}

public class DemoServer {

    public static void main(String[] args) {
        RpcProvider server = new RpcProvider("localhost", 8000);
        server
                .service("fib", Integer.class, new FibRequestHandler())
                .service("exp", ExpRequest.class, new ExpRequestHandler());
        server.start();
    }

}
