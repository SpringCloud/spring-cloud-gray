package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.compare.CompareMode;
import lombok.Getter;
import lombok.Setter;

public abstract class CompareGrayDecisionFactory<C extends CompareGrayDecisionFactory.CompareConfig> extends AbstractGrayDecisionFactory<C> {


    protected CompareGrayDecisionFactory(Class<C> configClass) {
        super(configClass);
    }

    @Setter
    @Getter
    public static class CompareConfig {
        private CompareMode compareMode;

    }

}
