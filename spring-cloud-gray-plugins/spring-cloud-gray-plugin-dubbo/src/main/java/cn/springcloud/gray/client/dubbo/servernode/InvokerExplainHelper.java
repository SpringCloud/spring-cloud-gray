package cn.springcloud.gray.client.dubbo.servernode;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.rpc.Invoker;

/**
 * @author saleson
 * @date 2020-09-10 12:48
 */
public class InvokerExplainHelper {

    public static String getServiceId(Invoker invoker) {
        URL url = invoker.getUrl();
        return url.getParameter(CommonConstants.REMOTE_APPLICATION_KEY);
    }


    public static String getAlias(Invoker invoker) {
        URL url = invoker.getUrl();
        String protocol = url.getProtocol();
        String address = url.getAddress();
        return protocol + ":" + address;
    }

}
