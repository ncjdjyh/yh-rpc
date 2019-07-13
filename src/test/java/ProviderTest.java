import provider.RpcProvider;

/**
 * @Author: neo
 * @FirstInitial: 2019/7/13
 * @Description: ~
 */
public class ProviderTest {
    public static void main(String[] args) {
        RpcProvider provider = new RpcProvider("localhost", 8000);
        provider.start();
    }
}
