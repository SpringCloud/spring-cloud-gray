package cn.springcloud.gray.choose;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.ServerListResult;
import cn.springcloud.gray.client.switcher.GraySwitcher;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerIdExtractor;
import cn.springcloud.gray.servernode.ServerListProcessor;
import cn.springcloud.gray.servernode.ServerSpec;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-08 21:02
 */
public class DefaultServerChooser implements ServerChooser<Object> {

    private GrayManager grayManager;
    private GraySwitcher graySwitcher;
    private ServerIdExtractor serverIdExtractor;
    private GrayServerSorter instanceGrayServerSorter;
    private GrayServerSorter serviceGrayServerSorter;
    private ServerExplainer serverExplainer;
    protected ServerListProcessor serverListProcessor;

    public DefaultServerChooser(
            GrayManager grayManager,
            GraySwitcher graySwitcher,
            ServerIdExtractor serverIdExtractor,
            ServerExplainer serverExplainer,
            GrayServerSorter instanceGrayServerSorter,
            GrayServerSorter serviceGrayServerSorter,
            ServerListProcessor serverListProcessor) {
        this.grayManager = grayManager;
        this.graySwitcher = graySwitcher;
        this.serverIdExtractor = serverIdExtractor;
        this.serverExplainer = serverExplainer;
        this.instanceGrayServerSorter = instanceGrayServerSorter;
        this.serviceGrayServerSorter = serviceGrayServerSorter;
        this.serverListProcessor = serverListProcessor;
    }

    @Override
    public Object chooseServer(List<Object> servers, ListChooser<Object> chooser) {
        if (!graySwitcher.state()) {
            return chooser.choose(servers);
        }

        String serviceId = serverIdExtractor.getServiceId(servers);
        List<Object> serverList = serverListProcessor.process(serviceId, servers);

        //如果有service级的灰度
        if (grayManager.hasServiceGray(serviceId)) {
            List<ServerSpec<Object>> serverSpecs = serverExplainer.apply(servers);
            ServerListResult<ServerSpec<Object>> serviceServerSpecListResult =
                    serviceGrayServerSorter.distinguishAndMatchGrayServerSpecList(serverSpecs);
            if (Objects.nonNull(serviceServerSpecListResult)) {
                return chooseServiceSpecServer(serviceServerSpecListResult, chooser);
            }
        }

        return chooseInstanceServer(serverList, chooser);
    }


    /**
     * Instance级别的筛选，返回最终的筛选结果
     *
     * @param servers
     * @param chooser
     * @return
     */
    protected Object chooseInstanceServer(List<Object> servers, ListChooser<Object> chooser) {
        ServerListResult<Object> serverListResult = instanceGrayServerSorter.distinguishAndMatchGrayServerList(servers);
        if (serverListResult == null) {
            return chooser.choose(servers);
        }

        if (GrayClientHolder.getGraySwitcher().isEanbleGrayRouting()
                && CollectionUtils.isNotEmpty(serverListResult.getGrayServers())) {
            Object server = chooser.choose(serverListResult.getGrayServers());
            if (server != null) {
                return server;
            }
        }

        return chooser.choose(serverListResult.getNormalServers());
    }

    /**
     * 先从Service级别将Server筛选出来，然后再经过Instance级别的筛选，返回最终的筛选结果
     *
     * @param serviceServerSpecListResult
     * @param chooser
     * @return
     */
    protected Object chooseServiceSpecServer(ServerListResult<ServerSpec<Object>> serviceServerSpecListResult, ListChooser<Object> chooser) {
        Object server = null;
        if (GrayClientHolder.getGraySwitcher().isEanbleGrayRouting()) {
            server = chooseInstanceSpecServer(serviceServerSpecListResult.getGrayServers(), chooser);
        }
        if (Objects.isNull(server)) {
            server = chooseInstanceSpecServer(serviceServerSpecListResult.getNormalServers(), chooser);
        }
        return server;
    }


    /**
     * 将从Service级别筛选出来的Server，再经过一次instance级别的筛选，返回最终的筛选结果
     *
     * @param serverSpecs
     * @param chooser
     * @return
     */
    protected Object chooseInstanceSpecServer(List<ServerSpec<Object>> serverSpecs, ListChooser<Object> chooser) {
        if (CollectionUtils.isEmpty(serverSpecs)) {
            return null;
        }

        ServerListResult<ServerSpec<Object>> serverSpecsListResult =
                instanceGrayServerSorter.distinguishAndMatchGrayServerSpecList(serverSpecs);
        if (serverSpecsListResult == null) {
            return chooseSpecServer(serverSpecs, chooser);
        }

        Object server = null;
        if (GrayClientHolder.getGraySwitcher().isEanbleGrayRouting()) {
            server = chooseSpecServer(serverSpecsListResult.getGrayServers(), chooser);
        }
        if (Objects.isNull(server)) {
            server = chooseSpecServer(serverSpecsListResult.getNormalServers(), chooser);
        }
        return server;
    }

    /**
     * 选择Server
     *
     * @param serverSpecs
     * @param chooser
     * @return
     */
    protected Object chooseSpecServer(List<ServerSpec<Object>> serverSpecs, ListChooser<Object> chooser) {
        List<Object> servers = collectServers(serverSpecs);
        return chooser.choose(servers);
    }


    /**
     * 将ServerSpec转换SERVER, @see {@link ServerExplainer#collectServers(List)}
     *
     * @param serverSpecs
     * @return
     */
    protected List<Object> collectServers(List<ServerSpec<Object>> serverSpecs) {
        return serverExplainer.collectServers(serverSpecs);
    }

}
