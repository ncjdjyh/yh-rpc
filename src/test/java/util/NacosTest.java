package util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.neo.yhrpc.common.NacosNamingService;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author ncjdjyh
 * @since 2022/8/17
 */
public class NacosTest {
    @Test
    public void registerInstance() throws NacosException {
        NamingService instance = NacosNamingService.getInstance();
        Assert.assertNotNull(instance);

        NamingService namingService = NamingFactory.createNamingService("127.0.0.1:8848");

        namingService.registerInstance("fib", "localhost", 8080);
        namingService.registerInstance("fib", "localhost", 8081);
        List<Instance> fib = namingService.getAllInstances("fib");
        System.out.println(JSONObject.toJSON(fib));
    }
}
