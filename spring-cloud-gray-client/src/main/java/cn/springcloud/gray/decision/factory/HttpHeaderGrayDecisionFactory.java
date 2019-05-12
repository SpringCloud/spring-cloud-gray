package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.compare.Comparators;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HttpHeaderGrayDecisionFactory
        extends CompareGrayDecisionFactory<HttpHeaderGrayDecisionFactory.Config> {

    public HttpHeaderGrayDecisionFactory() {
        super(Config.class);
    }

    @Override
    public GrayDecision apply(Config configBean) {
        return args -> {
            GrayHttpRequest grayRequest = (GrayHttpRequest) args.getGrayRequest();
            Map<String, ? extends Collection<String>> headers = grayRequest.getHeaders();
            return Comparators.getCollectionStringComparator(configBean.getCompareMode())
                    .test(headers.get(configBean.getHeader()), configBean.getValues());
        };
    }


    @Setter
    @Getter
    public static class Config extends CompareGrayDecisionFactory.CompareConfig {
        private String header;
        private List<String> values;

        public void setValues(List<String> values) {
            this.values = values;
            Collections.sort(values);
        }
    }
}
