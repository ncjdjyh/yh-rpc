package com.neo.yhrpc.provider;

import com.neo.yhrpc.common.IMessageHandler;
import com.neo.yhrpc.common.MessageInput;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

public class DefaultHandler implements IMessageHandler<MessageInput> {
	@Override
	public void handle(ChannelHandlerContext ctx, String requesetId, MessageInput input) {
		ctx.close();
	}

}
