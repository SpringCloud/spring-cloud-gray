package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.compare.Comparators;
import cn.springcloud.gray.decision.compare.PredicateComparator;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author saleson
 * @date 2020-09-12 18:46
 */
@Slf4j
public class AttributesGrayDecisionFactory extends CompareGrayDecisionFactory<AttributesGrayDecisionFactory.Config> {


    public AttributesGrayDecisionFactory() {
        super(AttributesGrayDecisionFactory.Config.class);
    }

    @Override
    public GrayDecision apply(Config configBean) {
        return args -> {
            GrayRequest grayRequest = args.getGrayRequest();
            PredicateComparator<Collection<String>> predicateComparator = Comparators.getCollectionStringComparator(configBean.getCompareMode());
            if (predicateComparator == null) {
                log.warn("[AttributesGrayDecision] serviceId:{} 没有找到相应与compareMode'{}'对应的PredicateComparator, testResult:{}",
                        grayRequest.getServiceId(), configBean.getCompareMode(), false);
                return false;
            }

            String attributeValue = grayRequest.getAttribute(configBean.getName());
            List<String> attributeValues = Arrays.asList(attributeValue);
            boolean b = predicateComparator.test(attributeValues, configBean.getValues());
            if (log.isDebugEnabled()) {
                log.debug("[AttributesGrayDecision] serviceId:{}, decisionConfig:{}, attributeValues:{}, testReslut:{}",
                        grayRequest.getServiceId(), JsonUtils.toJsonString(configBean), attributeValues, b);
            }
            return b;
        };
    }

    @Setter
    @Getter
    public static class Config extends CompareGrayDecisionFactory.CompareConfig {
        private String name;
        private List<String> values;

        public void setValues(List<String> values) {
            this.values = values;
            Collections.sort(values);
        }
    }
}
