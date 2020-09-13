package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.compare.Comparators;
import cn.springcloud.gray.decision.compare.PredicateComparator;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author saleson
 * @date 2020-09-12 18:42
 */
@Slf4j
public class AttributeGrayDecisionFactory extends CompareGrayDecisionFactory<AttributeGrayDecisionFactory.Config> {

    public AttributeGrayDecisionFactory() {
        super(AttributeGrayDecisionFactory.Config.class);
    }

    @Override
    public GrayDecision apply(Config configBean) {
        return args -> {
            GrayRequest grayRequest = args.getGrayRequest();
            PredicateComparator<String> predicateComparator = Comparators.getStringComparator(configBean.getCompareMode());
            if (predicateComparator == null) {
                log.warn("[AttributeGrayDecision] serviceId:{} 没有找到相应与compareMode'{}'对应的PredicateComparator, testResult:{}",
                        grayRequest.getServiceId(), configBean.getCompareMode(), false);
                return false;
            }

            String attributeValue = grayRequest.getAttribute(configBean.getName());
            boolean b = predicateComparator.test(attributeValue, configBean.getValue());
            if (log.isDebugEnabled()) {
                log.debug("[AttributeGrayDecision] serviceId:{}, decisionConfig:{}, attributeValue:{}, testReslut:{}",
                        grayRequest.getServiceId(), JsonUtils.toJsonString(configBean), attributeValue, b);
            }
            return b;
        };
    }

    @Setter
    @Getter
    public static class Config extends CompareGrayDecisionFactory.CompareConfig {
        private String name;
        private String value;

    }
}
