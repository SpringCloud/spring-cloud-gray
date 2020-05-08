package cn.springcloud.gray.choose;

import cn.springcloud.gray.servernode.ServerIdExtractor;

import java.util.List;

/**
 * @author saleson
 * @date 2020-05-08 20:22
 */
public abstract class AbstractGraySorter<SERVER> implements GraySorter<SERVER> {

    private ServerIdExtractor<SERVER> serverServerIdExtractor;


    public AbstractGraySorter(ServerIdExtractor<SERVER> serverServerIdExtractor) {
        this.serverServerIdExtractor = serverServerIdExtractor;
    }

    protected String getServiceId(List<SERVER> servers) {
        return serverServerIdExtractor.getServiceId(servers);
    }

    protected ServerIdExtractor<SERVER> getServerServerIdExtractor() {
        return serverServerIdExtractor;
    }

}
