package cn.springcloud.gray.client.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;


@Setter
@Getter
@ConfigurationProperties(prefix = "gray.request.track")
public class GrayTrackProperties {
    private boolean enabled = true;
    private String trackType = "web";
    private Web web = new Web();

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

        private Map<String, String> need = new HashMap<>();
    }

}
