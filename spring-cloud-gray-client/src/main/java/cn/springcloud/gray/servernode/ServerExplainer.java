package cn.springcloud.gray.servernode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Server解释器
 *
 * @param <SERVER>
 */
public interface ServerExplainer<SERVER> {

    VersionExtractor getVersionExtractor();


    /**
     * 将SERVER转换成{@link ServerSpec}
     *
     * @param server
     * @return
     */
    default ServerSpec<SERVER> apply(SERVER server) {
        Map metadata = getMetadata(server);
        return ServerSpec.<SERVER>builder()
                .server(server)
                .instanceId(getInstaceId(server))
                .serviceId(getServiceId(server))
                .metadata(metadata)
                .version(getVersion(server, metadata))
                .build();
    }

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

    /**
     * 获取server的metadata
     *
     * @param server
     * @return
     */
    Map getMetadata(SERVER server);


    default String getVersion(SERVER server) {
        return getVersion(server, getMetadata(server));
    }

    default String getVersion(SERVER server, Map metadata) {
        return getVersionExtractor().getVersion(getServiceId(server), server, metadata);
    }

}
