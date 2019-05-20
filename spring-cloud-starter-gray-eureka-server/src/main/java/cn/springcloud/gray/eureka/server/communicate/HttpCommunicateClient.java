package cn.springcloud.gray.eureka.server.communicate;

import cn.springcloud.gray.model.InstanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class HttpCommunicateClient implements GrayCommunicateClient {
    private static final Logger log = LoggerFactory.getLogger(HttpCommunicateClient.class);
    private final String baseUrl;
    private RestTemplate rest;

    public HttpCommunicateClient(String baseUrl) {
        this(baseUrl, new RestTemplate());
    }

    public HttpCommunicateClient(String baseUrl, RestTemplate rest) {
        this.baseUrl = baseUrl;
        this.rest = rest;
    }

    @Override
    public void noticeInstanceInfo(InstanceInfo instanceInfo) {
        String url = this.baseUrl + "/gray/discover/instanceInfo";

        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<InstanceInfo> httpEntity = new HttpEntity<>(instanceInfo, httpHeaders);
            rest.postForEntity(url, httpEntity, null);
        } catch (RuntimeException e) {
            log.error("实例信息发送失败", e);
            throw e;
        }
    }
}
