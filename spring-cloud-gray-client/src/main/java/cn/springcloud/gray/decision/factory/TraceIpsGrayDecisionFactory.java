package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Set;

public class TraceIpsGrayDecisionFactory extends AbstractGrayDecisionFactory<TraceIpsGrayDecisionFactory.Config> {

    private static final Logger log = LoggerFactory.getLogger(TraceIpsGrayDecisionFactory.class);

    public TraceIpsGrayDecisionFactory() {
        super(Config.class);
    }

    @Override
    public GrayDecision apply(Config configBean) {
        return args -> {
            GrayRequest grayRequest = args.getGrayRequest();
            GrayTrackInfo trackInfo = grayRequest.getGrayTrackInfo();
            String traceIp = trackInfo.getTraceIp();
            if (StringUtils.isEmpty(traceIp)) {
                log.warn("[TraceIpGrayDecision] serviceId:{} 灰度追踪记录的ip为空, testResult:{}",
                        grayRequest.getServiceId(), false);
                return false;
            }

            boolean b = predicateDecision(configBean, traceIp);
            if (log.isDebugEnabled()) {
                log.debug("[TraceIpGrayDecision] serviceId:{} config.ips:{}, traceIp:{}, testResult:{}",
                        grayRequest.getServiceId(), configBean.getIps(), traceIp, b);
            }
            return b;
        };
    }

    private boolean predicateDecision(Config configBean, String traceId) {
        switch (configBean.getCompareMode()) {
            case CONTAINS_ANY:
                return configBean.getIps().contains(traceId);
            case NOT_CONTAINS_ANY:
                return !configBean.getIps().contains(traceId);
            default:
                return false;
        }
    }

    /**
     * {@link Config#getCompareMode()} 仅支持CONTAINS_ANY和NOT_CONTAINS_ANY
     */
    @Setter
    @Getter
    public static class Config extends CompareGrayDecisionFactory.CompareConfig {
        private Set<String> ips;


    }
}
