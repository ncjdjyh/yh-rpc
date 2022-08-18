package com.neo.yhrpc.provider;

import com.neo.yhrpc.common.IMessageHandler;
import com.neo.yhrpc.common.MessageInput;
import io.netty.channel.ChannelHandlerContext;

public class DefaultHandler implements IMessageHandler<MessageInput> {
	@Override
	public void handle(ChannelHandlerContext ctx, String requesetId, MessageInput input) {
		ctx.close();
	}

}
