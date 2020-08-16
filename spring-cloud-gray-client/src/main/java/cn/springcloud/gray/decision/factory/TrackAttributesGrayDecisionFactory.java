package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.compare.Comparators;
import cn.springcloud.gray.decision.compare.PredicateComparator;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class TrackAttributesGrayDecisionFactory extends CompareGrayDecisionFactory<TrackAttributesGrayDecisionFactory.Config> {

    private static final Logger log = LoggerFactory.getLogger(TrackAttributesGrayDecisionFactory.class);

    public TrackAttributesGrayDecisionFactory() {
        super(Config.class);
    }


    @Override
    public GrayDecision apply(Config configBean) {
        return args -> {
            GrayRequest grayRequest = args.getGrayRequest();
            GrayTrackInfo grayTrackInfo = grayRequest.getGrayTrackInfo();
            if (grayTrackInfo == null) {
                log.warn("[TrackAttributesGrayDecision] serviceId:{} 没有获取到灰度追踪信息, testResult:{}",
                        grayRequest.getServiceId(), false);
                return false;
            }
            PredicateComparator<Collection<String>> predicateComparator = Comparators.getCollectionStringComparator(configBean.getCompareMode());
            if (predicateComparator == null) {
                log.warn("[TrackAttributesGrayDecision] serviceId:{} 没有找到相应与compareMode'{}'对应的PredicateComparator, testResult:{}",
                        grayRequest.getServiceId(), configBean.getCompareMode(), false);
                return false;
            }

            List<String> attributeValues = Arrays.asList(grayTrackInfo.getAttribute(configBean.getName()));
            boolean b = predicateComparator.test(attributeValues, configBean.getValues());
            if (log.isDebugEnabled()) {
                log.debug("[TrackAttributesGrayDecision] serviceId:{}, decisionConfig:{}, attributeValues:{}, testReslut:{}",
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
