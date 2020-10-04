package cn.springcloud.gray.client.dubbo.cluster.router;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.ServerListResult;
import cn.springcloud.gray.choose.ServerDistinguisher;
import cn.springcloud.gray.request.RequestLocalStorage;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.router.AbstractRouter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-09-10 21:45
 */
@Slf4j
public class GrayRouter extends AbstractRouter {


    public GrayRouter() {
    }

    @Override
    public <T> List<Invoker<T>> route(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        ServerDistinguisher<Invoker<T>> serverDistinguisher = GrayClientHolder.getServerDistinguisher();
        RequestLocalStorage requestLocalStorage = GrayClientHolder.getRequestLocalStorage();
        if (Objects.isNull(serverDistinguisher)
                || Objects.isNull(requestLocalStorage)
                || Objects.isNull(requestLocalStorage.getGrayRequest())) {
            return invokers;
        }
        try {
            return grayRoute(serverDistinguisher, invokers);
        } catch (Exception e) {
            log.warn("gray router invokers occur exception:{}, return original invokers, the invokers's size is {}.",
                    invokers.size(), e.getMessage(), e);
            return invokers;
        }
    }

    private <T> List<Invoker<T>> grayRoute(ServerDistinguisher<Invoker<T>> serverDistinguisher, List<Invoker<T>> invokers) {
        ServerListResult<Invoker<T>> result = serverDistinguisher.sensitivedistinguish(invokers);
        if (Objects.isNull(result)) {
            return invokers;
        }
        if (!result.getGrayServers().isEmpty()) {
            return result.getGrayServers();
        }
        if (!result.getNormalServers().isEmpty()) {
            return result.getNormalServers();
        }
        return Collections.emptyList();
    }
}
