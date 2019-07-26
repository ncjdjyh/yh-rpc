package com.neo.yhrpc.common;

import java.util.HashMap;
import java.util.Map;

public class MessageHandlers {
	private Map<String, IMessageHandler<?>> handlers = new HashMap<>();
	private Map<String, ReflectMessageHandler> reflectHandlers = new HashMap<>();
	private IMessageHandler<MessageInput> defaultHandler;

	public void register(String signature, IMessageHandler<?> handler) {
		handlers.put(signature, handler);
	}

	public void registerReflect(String signature, ReflectMessageHandler handler) {
		reflectHandlers.put(signature, handler);
	}

	public MessageHandlers defaultHandler(IMessageHandler<MessageInput> defaultHandler) {
		this.defaultHandler = defaultHandler;
		return this;
	}

	public IMessageHandler<MessageInput> defaultHandler() {
		return defaultHandler;
	}

	public IMessageHandler<?> get(String signature) {
		IMessageHandler<?> handler = handlers.get(signature);
		return handler;
	}

	public ReflectMessageHandler getReflect(String signature) {
		ReflectMessageHandler handler = reflectHandlers.get(signature);
		return handler;
	}
}
