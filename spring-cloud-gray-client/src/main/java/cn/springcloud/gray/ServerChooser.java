package cn.springcloud.gray;

import cn.springcloud.gray.choose.ListChooser;
import cn.springcloud.gray.servernode.ServerSpec;
import org.apache.commons.collections.CollectionUtils;

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

        if (GrayClientHolder.getGraySwitcher().isEanbleGrayRouting()) {
            if (CollectionUtils.isNotEmpty(serverListResult.getGrayServers())) {
                Server server = chooser.choose(serverListResult.getGrayServers());
                if (server != null) {
                    return server;
                }
            }
        }

        return chooser.choose(serverListResult.getNormalServers());
    }

}
