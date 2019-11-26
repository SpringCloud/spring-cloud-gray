package cn.springcloud.gray.server.module.client;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author saleson
 * @date 2019-11-25 20:38
 */
public interface ClientRemoteModule {

    /**
     * 获取客户端的路径
     *
     * @return
     */
    String getClientPath(String serviceId, String instanceId);


    /**
     * 调用客户端接口，并返回结果
     *
     * @param serviceId
     * @param instanceId
     * @param uri
     * @param function
     * @param <T>
     * @return
     */
    <T> T callClient(String serviceId, String instanceId, String uri, Function<String, T> function);


    /**
     * 调用客户端接口
     *
     * @param serviceId
     * @param instanceId
     * @param uri
     * @param consumer
     */
    void callClient(String serviceId, String instanceId, String uri, Consumer<String> consumer);
}
