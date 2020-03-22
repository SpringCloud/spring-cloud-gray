package cn.springcloud.gray.server.module.gray;

import cn.springcloud.gray.server.module.gray.domain.GrayModelType;

public interface GrayServiceIdFinder {

    String getServiceId(GrayModelType grayModelType, Object id);
}
