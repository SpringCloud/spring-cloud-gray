package cn.springcloud.gray;

import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerIdExtractor;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-08 20:27
 */
public class DefaultServerIdextractor implements ServerIdExtractor<Object> {


    private RequestLocalStorage requestLocalStorage;
    private ServerExplainer serverServerExplainer;


    public DefaultServerIdextractor(
            RequestLocalStorage requestLocalStorage,
            ServerExplainer serverServerExplainer) {
        this.requestLocalStorage = requestLocalStorage;
        this.serverServerExplainer = serverServerExplainer;
    }

    @Override
    public String getServiceId(Iterable<Object> servers) {
        GrayRequest grayRequest = requestLocalStorage.getGrayRequest();
        if (Objects.nonNull(grayRequest) && StringUtils.isNotEmpty(grayRequest.getServiceId())) {
            return grayRequest.getServiceId();
        }
        if (Objects.isNull(servers)) {
            return null;
        }
        Iterator<Object> serverIterator = servers.iterator();
        if (!serverIterator.hasNext()) {
            return null;
        }
        String serviceId = getServiceId(serverIterator.next());
        if (Objects.nonNull(grayRequest)) {
            grayRequest.setServiceId(serviceId);
        }
        return serviceId;
    }

    @Override
    public String getServiceId(Object server) {
        return serverServerExplainer.getServiceId(server);
    }

    @Override
    public String getInstaceId(Iterable<Object> servers) {
        if (Objects.isNull(servers)) {
            return null;
        }
        Iterator<Object> serverIterator = servers.iterator();
        if (!serverIterator.hasNext()) {
            return null;
        }
        return getInstaceId(serverIterator.next());
    }

    @Override
    public String getInstaceId(Object server) {
        return serverServerExplainer.getInstaceId(server);
    }
}
