package cn.springcloud.gray.servernode;

import java.util.List;

public interface ServerListProcessor<SRV> {

    List<SRV> process(String serviceId, List<SRV> servers);


    public static class Default<SRV> implements ServerListProcessor<SRV> {

        @Override
        public List<SRV> process(String serviceId, List<SRV> servers) {
            return servers;
        }
    }
}
