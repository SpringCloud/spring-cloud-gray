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

public class HttpParameterGrayDecisionFactory extends CompareGrayDecisionFactory<HttpParameterGrayDecisionFactory.Config> {


    public HttpParameterGrayDecisionFactory() {
        super(Config.class);
    }

    @Override
    public GrayDecision apply(Config configBean) {
        return args -> {
            GrayHttpRequest grayRequest = (GrayHttpRequest) args.getGrayRequest();
            return Comparators.getCollectionStringComparator(configBean.getCompareMode())
                    .test(grayRequest.getParameters().get(configBean.getName()), configBean.getValues());
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
