package cn.springcloud.gray.client.dubbo.servernode;

import java.util.Map;

/**
 * @author saleson
 * @date 2020-09-10 12:33
 */
public interface ServerMetadataExtractor<SERVER> {

    Map<String, String> getMetadata(SERVER server, String serviceId, String instanceId);


}
