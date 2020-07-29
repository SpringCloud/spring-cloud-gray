package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.compare.Comparators;
import cn.springcloud.gray.decision.compare.PredicateComparator;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HttpParameterGrayDecisionFactory extends CompareGrayDecisionFactory<HttpParameterGrayDecisionFactory.Config> {


    private static final Logger log = LoggerFactory.getLogger(HttpParameterGrayDecisionFactory.class);

    public HttpParameterGrayDecisionFactory() {
        super(Config.class);
    }

    @Override
    public GrayDecision apply(Config configBean) {
        return args -> {
            GrayHttpRequest grayRequest = (GrayHttpRequest) args.getGrayRequest();
            PredicateComparator<Collection<String>> predicateComparator =
                    Comparators.getCollectionStringComparator(configBean.getCompareMode());
            if (predicateComparator == null) {
                log.warn("没有找到相应与compareMode'{}'对应的PredicateComparator", configBean.getCompareMode());
                return false;
            }
            List<String> parameters = grayRequest.getParameter(configBean.getName());
            boolean b = predicateComparator.test(parameters, configBean.getValues());
            if (log.isDebugEnabled()) {
                log.debug("[HttpParameterGrayDecision] serviceId:{}, uri:{}, decisionConfig:{}, headerValues:{}, testResult:{}",
                        grayRequest.getServiceId(), grayRequest.getUri(), JsonUtils.toJsonString(configBean), grayRequest.getMethod(), b);
            }
            return b;
        };
    }


    @Setter
    @Getter
    @ToString
    public static class Config extends CompareGrayDecisionFactory.CompareConfig {
        private String name;
        private List<String> values;

        public void setValues(List<String> values) {
            this.values = values;
            Collections.sort(values);
        }
    }
}
