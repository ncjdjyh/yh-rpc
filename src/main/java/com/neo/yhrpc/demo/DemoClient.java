package com.neo.yhrpc.demo;

import com.neo.yhrpc.consumer.RpcConsumer;

public class DemoClient {

	private RpcConsumer client;

	public DemoClient(RpcConsumer client) {
		this.client = client;
		this.client.rpc("fib", Long.class).rpc("exp", ExpResponse.class).rpc("sum", Integer.class);
	}

	public long fib(int n) {
		return (Long) client.send("fib", n);
	}

	public ExpResponse exp(int base, int exp) {
		return (ExpResponse) client.send("exp", new ExpRequest(base, exp));
	}

	public static void main(String[] args) {
		RpcConsumer client = new RpcConsumer("rpcService");
		DemoClient demoClient = new DemoClient(client);
		long fib = demoClient.fib(100);
		System.out.println("remote call fib result:" + fib);
		client.close();
	}

}
