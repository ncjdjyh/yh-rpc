package util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.junit.Test;

import java.util.List;

/**
 * @author ncjdjyh
 * @since 2022/8/17
 */
public class NacosTest {
    @Test
    public void registerInstance() throws NacosException {

        NamingService namingService = NamingFactory.createNamingService("127.0.0.1:8848");

        namingService.registerInstance("fib", "8.8.8.8", 8089);
        namingService = NamingFactory.createNamingService("127.0.0.1:8848");
        namingService.registerInstance("fib", "127.0.0.1", 8010);
        List<Instance> fib = namingService.getAllInstances("fib");
//        Instance fib1 = RegisterCenter.getInstance("fib");
        System.out.println(JSONObject.toJSONString(fib));
    }
}
