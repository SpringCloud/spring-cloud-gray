package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.compare.Comparators;
import cn.springcloud.gray.decision.compare.PredicateComparator;
import cn.springcloud.gray.request.GrayHttpRequest;
import lombok.Getter;
import lombok.Setter;
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
                log.warn("没有找到相应与compareMode'{}'对应的PredicateComparator", configBean.getCompareMode());
                return false;
            }
            return predicateComparator.test(grayRequest.getMethod(), configBean.getMethod().name());
        };
    }


    @Setter
    @Getter
    public static class Config extends CompareGrayDecisionFactory.CompareConfig {
        private HttpMethod method;

    }
}
