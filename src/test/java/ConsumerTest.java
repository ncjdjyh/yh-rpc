import consumer.RpcConsumer;

/**
 * @Author: neo
 * @FirstInitial: 2019/7/13
 * @Description: ~
 */
public class ConsumerTest {
    public static void main(String[] args) {
        RpcConsumer consumer = new RpcConsumer("localhost", 8000);
        consumer.send("none", 8);
    }
}
