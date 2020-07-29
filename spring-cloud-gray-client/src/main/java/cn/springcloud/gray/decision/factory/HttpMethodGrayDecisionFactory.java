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
import org.springframework.http.HttpMethod;

public class HttpMethodGrayDecisionFactory extends AbstractGrayDecisionFactory<HttpMethodGrayDecisionFactory.Config> {


    private static final Logger log = LoggerFactory.getLogger(HttpMethodGrayDecisionFactory.class);


    public HttpMethodGrayDecisionFactory() {
        super(Config.class);
    }

    @Override
    public GrayDecision apply(Config configBean) {
        return args -> {
            GrayHttpRequest grayRequest = (GrayHttpRequest) args.getGrayRequest();
            PredicateComparator<String> predicateComparator = Comparators.getStringComparator(configBean.getCompareMode());
            if (predicateComparator == null) {
                log.warn("[HttpMethodGrayDecision] serviceId:{}, uri:{}, 没有找到相应与compareMode'{}'对应的PredicateComparator, testResult:{}",
                        grayRequest.getServiceId(), grayRequest.getUri(), configBean.getCompareMode(), false);
                return false;
            }
            boolean b = predicateComparator.test(grayRequest.getMethod(), configBean.getMethod().name());
            if (log.isDebugEnabled()) {
                log.debug("[HttpMethodGrayDecision] serviceId:{}, uri:{}, decisionConfig:{}, headerValues:{}, testResult:{}",
                        grayRequest.getServiceId(), grayRequest.getUri(), JsonUtils.toJsonString(configBean), grayRequest.getMethod(), b);
            }
            return b;
        };
    }


    @Setter
    @Getter
    @ToString
    public static class Config extends CompareGrayDecisionFactory.CompareConfig {
        private HttpMethod method;

    }
}
