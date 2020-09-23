package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TraceIpGrayDecisionFactory extends AbstractGrayDecisionFactory<TraceIpGrayDecisionFactory.Config> {

    private static final Logger log = LoggerFactory.getLogger(TraceIpGrayDecisionFactory.class);

    public TraceIpGrayDecisionFactory() {
        super(Config.class);
    }

    @Override
    public GrayDecision apply(Config configBean) {
        return args -> {
            Pattern pat = Pattern.compile(configBean.getIp());
            GrayRequest grayRequest = args.getGrayRequest();
            GrayTrackInfo trackInfo = grayRequest.getGrayTrackInfo();
            if (trackInfo == null) {
                log.warn("[TraceIpGrayDecision] serviceId:{} 没有获取到灰度追踪信息, testResult:{}",
                        grayRequest.getServiceId(), false);
                return false;
            }
            String traceIp = trackInfo.getTraceIp();
            if (StringUtils.isEmpty(traceIp)) {
                log.warn("[TraceIpGrayDecision] serviceId:{} 灰度追踪记录的ip为空, testResult:{}",
                        grayRequest.getServiceId(), false);
                return false;
            }
            Matcher mat = pat.matcher(traceIp);
            boolean b = mat.find();
            if (log.isDebugEnabled()) {
                log.debug("[TraceIpGrayDecision] serviceId:{} config.ip:{}, traceIp:{}, testResult:{}",
                        grayRequest.getServiceId(), configBean.getIp(), traceIp, b);
            }
            return b;
        };
    }

    @Setter
    @Getter
    public static class Config {
        private String ip;


    }
}
