package cn.springcloud.gray.helper;

import cn.springcloud.gray.ServerListResult;
import cn.springcloud.gray.servernode.ServerSpec;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-09-10 23:59
 */
public class ServerSpecHelper {


    public static <S> List<S> getServers(Collection<ServerSpec<S>> serverSpecs) {
        return serverSpecs.stream()
                .map(ServerSpec::getServer)
                .collect(Collectors.toList());
    }


    public static <S> ServerListResult<S> convertServerResult(ServerListResult<ServerSpec<S>> serverListResult) {
        List<S> grays = getServers(serverListResult.getGrayServers());
        List<S> normals = getServers(serverListResult.getNormalServers());
        return new ServerListResult<>(serverListResult.getServiceId(), grays, normals);
    }

}
