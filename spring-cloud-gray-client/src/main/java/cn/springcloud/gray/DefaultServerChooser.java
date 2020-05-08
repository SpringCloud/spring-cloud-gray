package cn.springcloud.gray;

import cn.springcloud.gray.choose.GraySorter;
import cn.springcloud.gray.choose.ListChooser;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @author saleson
 * @date 2020-05-08 21:02
 */
public class DefaultServerChooser implements ServerChooser<Object> {

    private GraySorter graySorter;

    public DefaultServerChooser(GraySorter graySorter) {
        this.graySorter = graySorter;
    }

    @Override
    public Object chooseServer(List<Object> servers, ListChooser<Object> chooser) {
        if (!GrayClientHolder.getGraySwitcher().state() || !GrayClientHolder.getGraySwitcher().isEanbleGrayRouting()) {
            return chooser.choose(servers);
        }


        ServerListResult<Object> serverListResult = graySorter.distinguishAndMatchGrayServerList(servers);
        if (serverListResult == null) {
            return chooser.choose(servers);
        }

        if (CollectionUtils.isNotEmpty(serverListResult.getGrayServers())) {
            Object server = chooser.choose(serverListResult.getGrayServers());
            if (server != null) {
                return server;
            }
        }

        return chooser.choose(serverListResult.getNormalServers());
    }
}
