package cn.springcloud.gray.server.module.gray;

public interface GrayServiceIdFinder {

    String getServiceId(GrayModelType grayModelType, Object id);
}
