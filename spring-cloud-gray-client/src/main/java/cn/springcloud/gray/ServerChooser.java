package cn.springcloud.gray;

import cn.springcloud.gray.choose.ListChooser;
import cn.springcloud.gray.servernode.ServerSpec;
import cn.springcloud.gray.utils.LogUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;

import java.util.List;

public interface ServerChooser<Server> {

    boolean matchGrayDecisions(ServerSpec serverSpec);

    boolean matchGrayDecisions(Server server);

    /**
     * 区分灰度实例和正常实例
     *
     * @param servers
     * @return 返回区分后的结果，如果关闭灰度开关，将会返回null
     */
    ServerListResult<Server> distinguishServerList(List<Server> servers);


    /**
     * 区分灰度实例和正常实例,并比对灰度实例的灰度决策
     *
     * @param servers
     * @return 返回区分及比对后的结果，如果关闭灰度开关，将会返回null
     */
    ServerListResult<Server> distinguishAndMatchGrayServerList(List<Server> servers);


    default Server chooseServer(List<Server> servers, ListChooser<Server> chooser) {
        ServerListResult<Server> serverListResult = distinguishAndMatchGrayServerList(servers);
        if (serverListResult == null) {
            return chooser.choose(servers);
        }

        Logger log = LogUtils.logger(ServerChooser.class);

        if (!GrayClientHolder.getGraySwitcher().isEanbleGrayRouting()) {
            log.warn("grayRouting未打开，从正常实例列表中挑选");
            return chooser.choose(serverListResult.getNormalServers());
        }

        if (CollectionUtils.isEmpty(serverListResult.getGrayServers())) {
            log.debug("没有匹配的灰度实例，从正常实例列表中挑选");
            return chooser.choose(serverListResult.getNormalServers());
        }

        log.debug("开始从灰度实例列表中负载挑选...");
        Server server = chooser.choose(serverListResult.getGrayServers());
        if (server != null) {
            log.debug("从灰度实例列表成功负载挑选到 {}", server);
            return server;
        }

        log.warn("从灰度实例列表负载挑选失败，从正常实例列表中挑选");
        return chooser.choose(serverListResult.getNormalServers());
    }

}
