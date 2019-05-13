package cn.springcloud.gray.servernode;

public interface ServerExplainer<INSTANCE> {


    ServerSpec apply(INSTANCE instance);


}
