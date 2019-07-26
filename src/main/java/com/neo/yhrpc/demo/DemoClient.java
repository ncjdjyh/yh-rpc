package com.neo.yhrpc.demo;

import com.neo.yhrpc.common.RPCException;
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

	public int sum(int a, int b) {
		Object[] x = {1, 2};
		return (Integer) client.send("sum", x);
	}

	public ExpResponse exp(int base, int exp) {
		return (ExpResponse) client.send("exp", new ExpRequest(base, exp));
	}

	public static void main(String[] args) throws InterruptedException {
		//RpcConsumer client = new RpcConsumer("localhost", 8000);
		//DemoClient demo = new DemoClient(client);
		//for (int i = 0; i < 30; i++) {
		//	try {
		//		System.out.printf("fib(%d) = %d\n", i, demo.fib(i));
		//		Thread.sleep(100);
		//	} catch (RPCException e) {
		//		i--; // retry
		//	}
		//}
		//for (int i = 0; i < 30; i++) {
		//	try {
		//		ExpResponse res = demo.exp(2, i);
		//		Thread.sleep(100);
		//		System.out.printf("exp2(%d) = %d cost=%dns\n", i, res.getValue(), res.getCostInNanos());
		//	} catch (RPCException e) {
		//		i--; // retry
		//	}
		//}
		//client.close();

		RpcConsumer client = new RpcConsumer("localhost", 8000);
		DemoClient demoClient = new DemoClient(client);
		int sum = demoClient.sum(1, 2);
		System.out.println(sum);
		client.close();
	}

}
