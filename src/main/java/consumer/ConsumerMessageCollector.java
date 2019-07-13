package consumer;

import common.MessageInput;
import common.MessageOutput;
import common.MessageRegistry;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: neo
 * @FirstInitial: 2019/7/13
 * @Description: ~
 */
public class ConsumerMessageCollector extends ChannelInboundHandlerAdapter {
    private final static Logger LOG = LoggerFactory.getLogger(ConsumerMessageCollector.class);

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
            LOG.error("unrecognized msg type {}", input.getType());
            return;
        }
        Object o = input.getPayload(clazz);
        RpcFuture<Object> future = (RpcFuture<Object>) pendingTasks.remove(input.getRequestId());
        if (future == null) {
            LOG.error("future not found with type {}", input.getType());
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
        RpcFuture<T> future = new RpcFuture<T>();
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
