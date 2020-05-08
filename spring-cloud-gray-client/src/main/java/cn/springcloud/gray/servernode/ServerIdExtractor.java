package cn.springcloud.gray.servernode;

/**
 * @author saleson
 * @date 2020-05-08 20:25
 */
public interface ServerIdExtractor<SERVER> {

    String getServiceId(Iterable<SERVER> servers);

    String getServiceId(SERVER server);

    String getInstaceId(Iterable<SERVER> servers);

    String getInstaceId(SERVER server);
}
