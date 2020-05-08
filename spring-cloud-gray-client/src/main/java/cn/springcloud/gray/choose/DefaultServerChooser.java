package cn.springcloud.gray.choose;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.ServerListResult;
import cn.springcloud.gray.client.switcher.GraySwitcher;
import cn.springcloud.gray.servernode.ServerIdExtractor;
import cn.springcloud.gray.servernode.ServerListProcessor;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @author saleson
 * @date 2020-05-08 21:02
 */
public class DefaultServerChooser implements ServerChooser<Object> {

    private GraySwitcher graySwitcher;
    private ServerIdExtractor serverIdExtractor;
    private GrayServerSorter instanceGrayServerSorter;
    private GrayServerSorter serviceGrayServerSorter;
    protected ServerListProcessor serverListProcessor;

    public DefaultServerChooser(
            GraySwitcher graySwitcher,
            ServerIdExtractor serverIdExtractor,
            GrayServerSorter instanceGrayServerSorter,
            GrayServerSorter serviceGrayServerSorter,
            ServerListProcessor serverListProcessor) {
        this.graySwitcher = graySwitcher;
        this.serverIdExtractor = serverIdExtractor;
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


        if (1 == 2) {
            serviceGrayServerSorter.distinguishAndMatchGrayServerList(serverList);
        }

        return chooseInstanceServer(serverList, chooser);
    }


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


//    protected Object chooseInstanceServer(List<Object> servers, ListChooser<Object> chooser) {
//        ServerListResult<Object> serverListResult = instanceGraySorter.distinguishAndMatchGrayServerList(servers);
//        if (serverListResult == null) {
//            return chooser.choose(servers);
//        }
//
//        if (GrayClientHolder.getGraySwitcher().isEanbleGrayRouting()
//                && CollectionUtils.isNotEmpty(serverListResult.getGrayServers())) {
//            Object server = chooser.choose(serverListResult.getGrayServers());
//            if (server != null) {
//                return server;
//            }
//        }
//
//        return chooser.choose(serverListResult.getNormalServers());
//    }
}
