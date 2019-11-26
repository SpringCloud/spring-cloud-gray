package cn.springcloud.gray.server.module.client;

import cn.springcloud.gray.exceptions.NotFoundException;
import cn.springcloud.gray.model.InstanceInfo;
import cn.springcloud.gray.server.discovery.ServiceDiscovery;
import cn.springcloud.gray.server.module.gray.GrayServerModule;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author saleson
 * @date 2019-11-25 20:46
 */
public class DefaultClientRemoteModule implements ClientRemoteModule {

    private ServiceDiscovery serviceDiscovery;
    private GrayServerModule grayServerModule;

    public DefaultClientRemoteModule(ServiceDiscovery serviceDiscovery, GrayServerModule grayServerModule) {
        this.serviceDiscovery = serviceDiscovery;
        this.grayServerModule = grayServerModule;
    }

    @Override
    public String getClientPath(String serviceId, String instanceId) {
        InstanceInfo instanceInfo = serviceDiscovery.getInstanceInfo(serviceId, instanceId);
        if (instanceInfo == null) {
            throw new NotFoundException();
        }
        if (StringUtils.isEmpty(instanceInfo.getHost()) || Objects.equals(instanceInfo.getPort(), 0)) {
            throw new IllegalArgumentException();
        }

        String contextPath = grayServerModule.getServiceContextPath(serviceId);
        StringBuilder path = new StringBuilder("http://")
                .append(instanceInfo.getHost())
                .append(":")
                .append(instanceInfo.getPort())
                .append(contextPath);
        return path.toString();
    }

    @Override
    public <T> T callClient(String serviceId, String instanceId, String uri, Function<String, T> function) {
        return function.apply(getClientPath(serviceId, instanceId) + uri);
    }

    @Override
    public void callClient(String serviceId, String instanceId, String uri, Consumer<String> consumer) {
        consumer.accept(getClientPath(serviceId, instanceId) + uri);
    }

}
