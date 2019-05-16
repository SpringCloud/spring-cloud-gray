package cn.springcloud.gray.client.config.properties;

import cn.springcloud.gray.model.GrayTrackDefinition;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@ConfigurationProperties(prefix = "gray.request.track")
public class GrayTrackProperties {
    private boolean enabled = true;
    private String trackType = "web";
    private Web web = new Web();

    private int definitionsUpdateIntervalTimerInMs = 0;

    @Setter
    @Getter
    public static class Web {

        public static final String NEED_URI = "uri";
        public static final String NEED_IP = "ip";
        public static final String NEED_METHOD = "method";
        public static final String NEED_HEADERS = "headers";
        public static final String NEED_PARAMETERS = "parameters";

        private String[] pathPatterns = new String[]{"/*"};
        private String[] excludePathPatterns = new String[]{};

        private List<GrayTrackDefinition> trackDefinitions = new ArrayList<>();

    }

}
