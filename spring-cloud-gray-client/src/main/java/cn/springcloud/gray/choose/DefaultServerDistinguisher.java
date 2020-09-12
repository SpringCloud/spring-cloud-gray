package cn.springcloud.gray.choose;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.ServerListResult;
import cn.springcloud.gray.client.switcher.GraySwitcher;
import cn.springcloud.gray.helper.ServerSpecHelper;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerIdExtractor;
import cn.springcloud.gray.servernode.ServerListProcessor;
import cn.springcloud.gray.servernode.ServerSpec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-09-10 23:23
 */
@Slf4j
public class DefaultServerDistinguisher implements ServerDistinguisher<Object> {

    private GrayManager grayManager;
    private GraySwitcher graySwitcher;
    private ServerIdExtractor serverIdExtractor;
    private GrayServerSorter instanceGrayServerSorter;
    private GrayServerSorter serviceGrayServerSorter;
    private ServerExplainer serverExplainer;
    protected ServerListProcessor serverListProcessor;


    public DefaultServerDistinguisher(
            GrayManager grayManager,
            GraySwitcher graySwitcher,
            ServerIdExtractor serverIdExtractor,
            GrayServerSorter instanceGrayServerSorter,
            GrayServerSorter serviceGrayServerSorter,
            ServerExplainer serverExplainer,
            ServerListProcessor serverListProcessor) {
        this.grayManager = grayManager;
        this.graySwitcher = graySwitcher;
        this.serverIdExtractor = serverIdExtractor;
        this.instanceGrayServerSorter = instanceGrayServerSorter;
        this.serviceGrayServerSorter = serviceGrayServerSorter;
        this.serverExplainer = serverExplainer;
        this.serverListProcessor = serverListProcessor;
    }

    @Override
    public ServerListResult<Object> sensitivedistinguish(List<Object> servers) {
        if (!graySwitcher.state()) {
            log.debug("灰度未开启，返回null");
            return null;
        }

        return distinguish(servers);

    }

    @Override
    public ServerListResult<Object> distinguish(List<Object> servers) {
        String serviceId = serverIdExtractor.getServiceId(servers);
        if (!grayManager.isNeedDistinguish(serviceId)) {
            return null;
        }

        List<Object> serverList = serverListProcessor.process(serviceId, servers);

        List<ServerSpec<Object>> serverSpecs = serverExplainer.apply(servers);

        ServerListResult<ServerSpec<Object>> result = null;

        if (grayManager.hasServiceGray(serviceId)) {
            log.debug("{} 服务有服务灰度策略, 从serverList列表进行灰度筛选区分, serverList.size={}", serviceId, serverList.size());
            result = allLevelDistinguish(serverSpecs);
        } else {
            log.debug("{} 服务没有服务灰度策略, 从serverList列表进行灰度筛选区分, serverList.size={}", serviceId, serverList.size());
            result = instanceLevelDistinguish(serverSpecs);
        }

        if (Objects.isNull(result)) {
            log.debug("区分 {} 服务灰度列表和正常列表失败, result返回null, serverList.size={}", serviceId, serverList.size());
            return null;
        }

        return ServerSpecHelper.convertServerResult(result);
    }


    private ServerListResult<ServerSpec<Object>> allLevelDistinguish(List<ServerSpec<Object>> serverSpecs) {
        ServerListResult<ServerSpec<Object>> serviceLevelResult = serviceLevelDistinguish(serverSpecs);
        if (Objects.isNull(serviceLevelResult)) {
            return null;
        }
        List<ServerSpec<Object>> grayServers = serviceLevelResult.getGrayServers();
        if (grayServers.isEmpty()) {
            return instanceLevelDistinguish(serviceLevelResult.getNormalServers());
        }

        ServerListResult<ServerSpec<Object>> instanceLevelGrayResult = instanceLevelDistinguish(grayServers);
        boolean isInstanceLevelGrayResultEmpty = Objects.isNull(instanceLevelGrayResult)
                || instanceLevelGrayResult.getGrayServers().isEmpty()
                || instanceLevelGrayResult.getNormalServers().isEmpty();

        if (!isInstanceLevelGrayResultEmpty) {
            return instanceLevelGrayResult;
        }

        return instanceLevelDistinguish(serviceLevelResult.getNormalServers());
    }


    private ServerListResult<ServerSpec<Object>> instanceLevelDistinguish(List<ServerSpec<Object>> serverSpecs) {
        return distinguish(instanceGrayServerSorter, serverSpecs);
    }

    private ServerListResult<ServerSpec<Object>> serviceLevelDistinguish(List<ServerSpec<Object>> serverSpecs) {
        return distinguish(serviceGrayServerSorter, serverSpecs);
    }

    private ServerListResult<ServerSpec<Object>> distinguish(GrayServerSorter<Object> grayServerSorter, List<ServerSpec<Object>> serverSpecs) {
        return grayServerSorter.distinguishAndMatchGrayServerSpecList(serverSpecs);
    }


}
