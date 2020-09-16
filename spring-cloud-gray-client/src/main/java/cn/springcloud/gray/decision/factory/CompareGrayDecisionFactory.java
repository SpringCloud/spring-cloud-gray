package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.compare.Comparators;
import cn.springcloud.gray.decision.compare.CompareMode;
import cn.springcloud.gray.decision.compare.PredicateComparator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
public abstract class CompareGrayDecisionFactory<C extends CompareGrayDecisionFactory.CompareConfig> extends AbstractGrayDecisionFactory<C> {


    protected CompareGrayDecisionFactory(Class<C> configClass) {
        super(configClass);
    }


    protected PredicateComparator<Collection<String>> getCollectionStringComparator(CompareMode mode, String serviceId) {
        PredicateComparator<Collection<String>> predicateComparator = Comparators.getCollectionStringComparator(mode);
        if (predicateComparator == null) {
            log.warn("[{}] serviceId:{} 没有找到相应与compareMode'{}'对应的PredicateComparator, testResult:{}",
                    getClass().getSimpleName(), serviceId, mode, false);
            return null;
        }
        return predicateComparator;
    }


    protected PredicateComparator<String> getStringComparator(CompareMode mode, String serviceId) {
        PredicateComparator<String> predicateComparator = Comparators.getStringComparator(mode);
        if (predicateComparator == null) {
            log.warn("[{}] serviceId:{} 没有找到相应与compareMode'{}'对应的PredicateComparator, testResult:{}",
                    getClass().getSimpleName(), serviceId, mode, false);
            return null;
        }
        return predicateComparator;
    }


    @Setter
    @Getter

    public static class CompareConfig {
        private CompareMode compareMode;

    }

}
