package cn.springcloud.gray.communication;

import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayTrackDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpInformationClient implements InformationClient {
    private static final Logger log = LoggerFactory.getLogger(HttpInformationClient.class);
    private final String baseUrl;
    private RestTemplate rest;

    public HttpInformationClient(String baseUrl) {
        this(baseUrl, new RestTemplate());
    }

    public HttpInformationClient(String baseUrl, RestTemplate rest) {
        this.baseUrl = baseUrl;
        this.rest = rest;
    }

    @Override
    public List<GrayInstance> allGrayInstances() {
        String url = this.baseUrl + "/gray/instances/enable";
        ParameterizedTypeReference<List<GrayInstance>> typeRef = new ParameterizedTypeReference<List<GrayInstance>>() {
        };

        try {
            ResponseEntity<List<GrayInstance>> responseEntity = rest.exchange(url, HttpMethod.GET, null, typeRef);
            return responseEntity.getBody();
        } catch (RuntimeException e) {
            log.error("获取灰度服务列表失败", e);
            throw e;
        }
    }


    @Override
    public void addGrayInstance(GrayInstance grayInstance) {
        String url = this.baseUrl + "/gray/instance/";
        try {
            rest.postForEntity(url, grayInstance, null);
        } catch (RuntimeException e) {
            log.error("灰度服务实例下线失败", e);
            throw e;
        }
    }

    @Override
    public GrayInstance getGrayInstance(String serviceId, String instanceId) {
        String url = this.baseUrl + "/gray/instance?serviceId={serviceId}&instanceId={instanceId}";
        try {
            ResponseEntity<GrayInstance> responseEntity =
                    rest.getForEntity(url, GrayInstance.class, serviceId, instanceId);
            return responseEntity.getBody();
        } catch (RuntimeException e) {
            log.error("获取灰度实例", e);
            throw e;
        }
    }

    @Override
    public void serviceDownline(String instanceId) {
        String url = this.baseUrl + "/gray/instance/{id}/switchStatus?switch=0";
        Map<String, String> params = new HashMap<>();
        params.put("instanceId", instanceId);
        try {
            rest.delete(url, params);
        } catch (Exception e) {
            log.error("灰度服务实例下线失败", e);
            throw e;
        }
    }

    @Override
    public List<GrayTrackDefinition> getTrackDefinitions(String serviceId, String instanceId) {
        String url = this.baseUrl + "/gray/trackDefinitions?serviceId={serviceId}&instanceId={instanceId}";
        ParameterizedTypeReference<List<GrayTrackDefinition>> typeRef = new ParameterizedTypeReference<List<GrayTrackDefinition>>() {
        };
        try {
            ResponseEntity<List<GrayTrackDefinition>> responseEntity =
                    rest.exchange(url, HttpMethod.GET, null, typeRef, serviceId, instanceId);
            return responseEntity.getBody();
        } catch (RuntimeException e) {
            log.error("获取灰度追踪信息", e);
            throw e;
        }
    }
}
