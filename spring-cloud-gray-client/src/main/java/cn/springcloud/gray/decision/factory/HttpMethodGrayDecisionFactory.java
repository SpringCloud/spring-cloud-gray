package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayRequest;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

import java.util.Objects;

public class HttpMethodGrayDecisionFactory extends AbstractGrayDecisionFactory<HttpMethodGrayDecisionFactory.Config> {


    public HttpMethodGrayDecisionFactory() {
        super(Config.class);
    }

    @Override
    public GrayDecision apply(Config configBean) {
        return args -> {
            GrayHttpRequest grayRequest = (GrayHttpRequest) args.getGrayRequest();
            return StringUtils.endsWithIgnoreCase(grayRequest.getMethod(), configBean.getMethod().name());
        };
    }


    @Setter
    @Getter
    public static class Config {
        private HttpMethod method;

    }
}
