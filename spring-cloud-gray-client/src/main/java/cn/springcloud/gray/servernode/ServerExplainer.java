package cn.springcloud.gray.servernode;

public interface ServerExplainer<SERVER> {


    ServerSpec apply(SERVER server);

    String getServiceId(SERVER server);

    String getInstaceId(SERVER server);

}
