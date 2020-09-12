package cn.springcloud.gray.choose;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.ServerListResult;
import cn.springcloud.gray.decision.GrayDecisionInputArgs;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerIdExtractor;
import cn.springcloud.gray.servernode.ServerSpec;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-05-08 20:22
 */
public abstract class AbstractGrayServerSorter<SERVER> implements GrayServerSorter<SERVER> {

    private GrayManager grayManager;
    private RequestLocalStorage requestLocalStorage;
    private ServerIdExtractor<SERVER> serverServerIdExtractor;
    private ServerExplainer<SERVER> serverExplainer;

    public AbstractGrayServerSorter(
            GrayManager grayManager,
            RequestLocalStorage requestLocalStorage,
            ServerIdExtractor<SERVER> serverServerIdExtractor,
            ServerExplainer<SERVER> serverExplainer) {
        this.grayManager = grayManager;
        this.requestLocalStorage = requestLocalStorage;
        this.serverServerIdExtractor = serverServerIdExtractor;
        this.serverExplainer = serverExplainer;
    }

    @Override
    public ServerListResult<SERVER> distinguishServerList(List<SERVER> servers) {
        return distinguishOrMatchServerList(servers, this::distinguishServerSpecList);
    }


    @Override
    public ServerListResult<SERVER> distinguishAndMatchGrayServerList(List<SERVER> servers) {
        return distinguishOrMatchServerList(servers, this::distinguishAndMatchGrayServerSpecList);
    }


    @Override
    public ServerListResult<ServerSpec<SERVER>> distinguishServerSpecList(List<ServerSpec<SERVER>> serverSpecs) {
        String serviceId = getSpecServiceId(serverSpecs);
        if (!grayManager.isNeedDistinguish(serviceId)) {
            return null;
        }
        return distinguishServerSpecList(serviceId, serverSpecs);
    }


    @Override
    public ServerListResult<ServerSpec<SERVER>> distinguishAndMatchGrayServerSpecList(List<ServerSpec<SERVER>> serverSpecs) {
        ServerListResult<ServerSpec<SERVER>> serverSpecResult = distinguishServerSpecList(serverSpecs);
        if (Objects.isNull(serverSpecResult)) {
            return null;
        }
        if (GrayClientHolder.getGraySwitcher().isEanbleGrayRouting()
                || CollectionUtils.isNotEmpty(serverSpecResult.getGrayServers())) {
            List<ServerSpec<SERVER>> grays = filterServerSpecAccordingToRoutePolicy(
                    serverSpecResult.getServiceId(), serverSpecResult.getGrayServers());
            serverSpecResult.setGrayServers(grays);
        } else {
            serverSpecResult.setGrayServers(ListUtils.EMPTY_LIST);
        }
        return serverSpecResult;
    }


    /**
     * 将SERVER转换成ServerSpec进行区分(匹配),然后再转换回SERVER返回
     *
     * @param servers
     * @param spectServerListfunction
     * @return
     */
    protected ServerListResult<SERVER> distinguishOrMatchServerList(
            List<SERVER> servers,
            Function<List<ServerSpec<SERVER>>, ServerListResult<ServerSpec<SERVER>>> spectServerListfunction) {

        String serviceId = getServiceId(servers);
        if (!grayManager.isNeedDistinguish(serviceId)) {
            return null;
        }

        //转换
        List<ServerSpec<SERVER>> serverSpecs = serverExplainer.apply(servers);

        //区分灰度/正常
        ServerListResult<ServerSpec<SERVER>> serverSpecResult = spectServerListfunction.apply(serverSpecs);

        //转换
        List<SERVER> grayServers = serverSpecResult.getGrayServers()
                .stream()
                .map(ServerSpec::getServer)
                .collect(Collectors.toList());
        List<SERVER> normalServers = serverSpecResult.getNormalServers()
                .stream()
                .map(ServerSpec::getServer)
                .collect(Collectors.toList());
        return new ServerListResult<>(serviceId, grayServers, normalServers);
    }


    /**
     * 区分灰度/正常的Server
     *
     * @param serviceId
     * @param serverSpecs
     * @return
     */
    protected abstract ServerListResult<ServerSpec<SERVER>> distinguishServerSpecList(
            String serviceId, List<ServerSpec<SERVER>> serverSpecs);


    /**
     * 根据路由策略过滤并返回
     *
     * @param serviceId
     * @param serverSpecs
     * @return
     */
    protected abstract List<ServerSpec<SERVER>> filterServerSpecAccordingToRoutePolicy(
            String serviceId, List<ServerSpec<SERVER>> serverSpecs);


    protected GrayDecisionInputArgs createDecisionInputArgs(ServerSpec serverSpec) {
        GrayDecisionInputArgs decisionInputArgs = new GrayDecisionInputArgs();
        decisionInputArgs.setServer(serverSpec);
        decisionInputArgs.setGrayRequest(requestLocalStorage.getGrayRequest());
        return decisionInputArgs;
    }

    /**
     * 获取serviceId
     *
     * @param servers
     * @return
     */
    protected String getServiceId(Iterable<SERVER> servers) {
        return serverServerIdExtractor.getServiceId(servers);
    }

    /**
     * 获取serviceId
     *
     * @param serverSpecs
     * @return
     */
    protected String getSpecServiceId(Iterable<ServerSpec<SERVER>> serverSpecs) {
        return serverServerIdExtractor.getSpecServiceId(serverSpecs);
    }


    public GrayManager getGrayManager() {
        return grayManager;
    }


    protected ServerIdExtractor<SERVER> getServerServerIdExtractor() {
        return serverServerIdExtractor;
    }


    public ServerExplainer<SERVER> getServerExplainer() {
        return serverExplainer;
    }

    public RequestLocalStorage getRequestLocalStorage() {
        return requestLocalStorage;
    }
}
