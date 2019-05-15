package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.request.GrayTrackInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TraceIpGrayDecisionFactory extends AbstractGrayDecisionFactory<TraceIpGrayDecisionFactory.Config> {


    public TraceIpGrayDecisionFactory() {
        super(Config.class);
    }

    @Override
    public GrayDecision apply(Config configBean) {
        return args -> {
            Pattern pat = Pattern.compile(configBean.getIp());
            GrayTrackInfo trackInfo = args.getGrayRequest().getGrayTrackInfo();
            if (trackInfo == null) {
                return false;
            }
            String traceIp = trackInfo.getTraceIp();
            if (StringUtils.isEmpty(traceIp)) {
                return false;
            }
            Matcher mat = pat.matcher(traceIp);
            return mat.find();
        };
    }

    @Setter
    @Getter
    public static class Config {
        private String ip;


    }
}
