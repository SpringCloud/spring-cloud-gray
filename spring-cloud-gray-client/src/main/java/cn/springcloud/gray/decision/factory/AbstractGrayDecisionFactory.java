package cn.springcloud.gray.decision.factory;

import org.springframework.beans.BeanUtils;

public abstract class AbstractGrayDecisionFactory<C> implements GrayDecisionFactory<C> {

    private Class<C> configClass;

    protected AbstractGrayDecisionFactory(Class<C> configClass) {
        this.configClass = configClass;
    }

    public Class<C> getConfigClass() {
        return configClass;
    }

    public C newConfig() {
        return BeanUtils.instantiateClass(getConfigClass());
    }
}
