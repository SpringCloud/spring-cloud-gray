package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.utils.NameUtils;

import java.util.function.Consumer;

/**
 * 灰度决策的工厂类
 */
public interface GrayDecisionFactory<C> {


    default String name() {
        return NameUtils.normalizeDecisionFactoryName(getClass());
    }

    default C newConfig() {
        throw new UnsupportedOperationException("newConfig() not implemented");
    }

    default GrayDecision apply(Consumer<C> consumer) {
        C config = newConfig();
        consumer.accept(config);
        return apply(config);
    }

    GrayDecision apply(C configBean);
}
