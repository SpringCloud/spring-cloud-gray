package cn.springcloud.gray.choose;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.ServerListResult;
import cn.springcloud.gray.client.switcher.GraySwitcher;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerIdExtractor;
import cn.springcloud.gray.servernode.ServerListProcessor;
import cn.springcloud.gray.servernode.ServerSpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-08 21:02
 */
@Slf4j
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
            log.debug("灰度未开启，从servers列表挑选");
            return chooser.choose(ChooseGroup.ALL, servers);
        }

        String serviceId = serverIdExtractor.getServiceId(servers);
        List<Object> serverList = serverListProcessor.process(serviceId, servers);

        if (!grayManager.hasServiceGray(serviceId)) {
            log.debug("{} 服务没有相关灰度策略, 从serverList列表进行灰度策选, serverList.size={}", serviceId, serverList.size());
            return chooseInstanceServer(serverList, chooser);
        }


        List<ServerSpec<Object>> serverSpecs = serverExplainer.apply(servers);
        ServerListResult<ServerSpec<Object>> serviceServerSpecListResult =
                serviceGrayServerSorter.distinguishAndMatchGrayServerSpecList(serverSpecs);

        if (Objects.isNull(serviceServerSpecListResult)) {
            log.debug("区分 {} 服务灰度列表和正常列表失败, 从serverList列表进行灰度策选, serverList.size={}", serviceId, serverList.size());
            return chooseInstanceServer(serverList, chooser);
        }

        return chooseServiceSpecServer(serviceServerSpecListResult, chooser);
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
            return chooser.choose(ChooseGroup.ALL, servers);
        }

        if (GrayClientHolder.getGraySwitcher().isEanbleGrayRouting()
                && CollectionUtils.isNotEmpty(serverListResult.getGrayServers())) {
            Object server = chooser.choose(ChooseGroup.GRAY, serverListResult.getGrayServers());
            if (server != null) {
                return server;
            }
        }

        return chooser.choose(ChooseGroup.NORMAL, serverListResult.getNormalServers());
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
            log.debug("从{}服务的灰度实例列表中挑选到 {}", serviceServerSpecListResult.getServiceId(), server);
        }
        if (Objects.isNull(server)) {
            server = chooseInstanceSpecServer(serviceServerSpecListResult.getNormalServers(), chooser);
            log.debug("从{}服务的正常实例列表中挑选到 {}", serviceServerSpecListResult.getServiceId(), server);
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
            return chooseSpecServer(ChooseGroup.ALL, serverSpecs, chooser);
        }

        Object server = null;
        if (GrayClientHolder.getGraySwitcher().isEanbleGrayRouting()) {
            server = chooseSpecServer(ChooseGroup.GRAY, serverSpecsListResult.getGrayServers(), chooser);
        }
        if (Objects.isNull(server)) {
            server = chooseSpecServer(ChooseGroup.NORMAL, serverSpecsListResult.getNormalServers(), chooser);
        }
        return server;
    }

    /**
     * 选择Server
     *
     * @param chooseGroup
     * @param serverSpecs
     * @param chooser
     * @return
     */
    protected Object chooseSpecServer(ChooseGroup chooseGroup, List<ServerSpec<Object>> serverSpecs, ListChooser<Object> chooser) {
        List<Object> servers = collectServers(serverSpecs);
        return chooser.choose(chooseGroup, servers);
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
