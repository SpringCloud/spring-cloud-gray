package cn.springcloud.gray;

import cn.springcloud.gray.client.GrayClientAppContext;
import cn.springcloud.gray.core.GrayInstance;
import cn.springcloud.gray.core.GrayService;
import cn.springcloud.gray.core.InformationClient;
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

    public HttpInformationClient(String baseUrl, RestTemplate rest) {
        this.baseUrl = baseUrl;
        this.rest = rest;
    }

    @Override
    public List<GrayService> listGrayService() {
        String url = this.baseUrl + "/gray/services/enable";
        ParameterizedTypeReference<List<GrayService>> typeRef = new ParameterizedTypeReference<List<GrayService>>() {
        };

        try {
            ResponseEntity<List<GrayService>> responseEntity = rest.exchange(url, HttpMethod.GET, null, typeRef);
            return responseEntity.getBody();
        } catch (RuntimeException e) {
            log.error("获取灰度服务列表失败", e);
            throw e;
        }
    }

    @Override
    public GrayService grayService(String serviceId) {
        String url = this.baseUrl + "/gray/services/{serviceId}";
        Map<String, String> params = new HashMap<>();
        params.put("serviceId", serviceId);

        try {
            ResponseEntity<GrayService> responseEntity = rest.getForEntity(url, GrayService.class, params);
            return responseEntity.getBody();
        } catch (RuntimeException e) {
            log.error("获取灰度服务失败", e);
            throw e;
        }
    }

    @Override
    public GrayInstance grayInstance(String serviceId, String instanceId) {
        String url = this.baseUrl + "/gray/services/{serviceId}/instance/?instanceId={instanceId}";

        Map<String, String> params = new HashMap<>();
        params.put("serviceId", serviceId);
        params.put("instanceId", instanceId);
        try {
            ResponseEntity<GrayInstance> responseEntity = rest.getForEntity(url, GrayInstance.class, params);
            return responseEntity.getBody();
        } catch (RuntimeException e) {
            log.error("获取灰度服务实例失败", e);
            throw e;
        }
    }

    @Override
    public void addGrayInstance(String serviceId, String instanceId) {
        GrayInstance grayInstance = new GrayInstance();
        grayInstance.setInstanceId(instanceId);
        grayInstance.setServiceId(serviceId);

        String url = this.baseUrl + "/gray/services/{serviceId}/instance";
        try {
            rest.postForEntity(url, grayInstance, null, serviceId);
        } catch (RuntimeException e) {
            log.error("灰度服务实例下线失败", e);
            throw e;
        }
    }

    @Override
    public void serviceDownline() {
        InstanceLocalInfo localInfo = GrayClientAppContext.getInstanceLocalInfo();
        if (!localInfo.isGray()) {
            return;
        }

        serviceDownline(localInfo.getServiceId(), localInfo.getInstanceId());
    }

    @Override
    public void serviceDownline(String serviceId, String instanceId) {
        String url = this.baseUrl + "/gray/services/{serviceId}/instance/?instanceId={instanceId}";
        Map<String, String> params = new HashMap<>();
        params.put("serviceId", serviceId);
        try {
            params.put("instanceId", instanceId);
            rest.delete(url, params);
        } catch (Exception e) {
            log.error("灰度服务实例下线失败", e);
            throw e;
        }
    }
}
