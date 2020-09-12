package cn.springcloud.gray.client.dubbo.cluster.factory;

import cn.springcloud.gray.client.dubbo.cluster.router.GrayRouter;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.cluster.CacheableRouterFactory;
import org.apache.dubbo.rpc.cluster.Router;

/**
 * @author saleson
 * @date 2020-09-10 21:39
 */
@Activate(order = 100000)
public class GrayRouterFactory extends CacheableRouterFactory {

    public static final String NAME = "gray";

    @Override
    protected Router createRouter(URL url) {
        return new GrayRouter();
    }

//    private ServerDistinguisher getServerDistinguisher() {
//        if (Objects.isNull(serverDistinguisher)) {
//            serverDistinguisher = SpringApplicationContextUtils.getBean(
//                    "serverDistinguisher", ServerDistinguisher.class);
//        }
//        return serverDistinguisher;
//    }
}
