package cn.springcloud.gray.servernode;

public interface ServerExplainer<SERVER> {


    ServerSpec<SERVER> apply(SERVER server);

    String getServiceId(SERVER server);

    String getInstaceId(SERVER server);

}
