package cn.springcloud.gray.servernode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Server解释器
 *
 * @param <SERVER>
 */
public interface ServerExplainer<SERVER> {


    /**
     * 将SERVER转换成{@link ServerSpec}
     *
     * @param server
     * @return
     */
    ServerSpec<SERVER> apply(SERVER server);

    /**
     * 将SERVER转换成{@link ServerSpec}
     *
     * @param servers
     * @return
     */
    default List<ServerSpec<SERVER>> apply(List<SERVER> servers) {
        return servers.stream()
                .map(this::apply)
                .collect(Collectors.toList());
    }


    /**
     * 将ServerSpec转换SERVER, @see {@link ServerSpec#getServer()}
     *
     * @param serverSpecs
     * @return
     */
    default List<SERVER> collectServers(List<ServerSpec<SERVER>> serverSpecs) {
        return serverSpecs.stream().map(ServerSpec::getServer).collect(Collectors.toList());
    }

    /**
     * 提取SERVER的serviceId
     *
     * @param server
     * @return
     */
    String getServiceId(SERVER server);

    /**
     * 提取SERVER的instanceId
     *
     * @param server
     * @return
     */
    String getInstaceId(SERVER server);

}
