package cn.springcloud.gray.server.module;

import cn.springcloud.gray.server.module.gray.domain.GrayModelType;

public interface NamespaceFinder {

    String getNamespaceCode(GrayModelType grayModelType, Object id);

    String getNamespaceCode(Class<?> modelCls, Object id);

    boolean hasResource(String namespace);
}
