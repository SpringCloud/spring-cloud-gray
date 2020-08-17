package cn.springcloud.gray.servernode;

import java.util.Map;

/**
 * @author saleson
 * @date 2020-05-11 23:18
 */
public interface VersionExtractor<SERVER> {

    String getVersion(String serviceId, SERVER server, Map metadata);

}
