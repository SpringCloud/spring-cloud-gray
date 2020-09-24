package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayTrackDefinition;
import cn.springcloud.gray.server.module.client.ClientRemoteModule;
import cn.springcloud.gray.server.utils.ApiResHelper;
import cn.springcloud.gray.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author saleson
 * @date 2019-11-24 14:40
 */
@RestController
@RequestMapping("/gray/client/grayList")
public class ClientGrayListResource {

    @Autowired
    private ClientRemoteModule clientRemoteModule;

    @Autowired
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, String> infoUrls = new HashMap<>();

    @PostConstruct
    public void init() {
        infoUrls.put("POLICY", "/gray/list/policy/allInfos");
    }

    @GetMapping("/track/allDefinitions")
    public ApiRes<List<GrayTrackDefinition>> getAllGrayTracks(
            @RequestParam("serviceId") String serviceId,
            @RequestParam("instanceId") String instanceId) {
        return clientRemoteModule.callClient(serviceId, instanceId,
                "/gray/list/track/allDefinitions", url -> {
                    return restTemplate.execute(url, HttpMethod.GET, null, res -> {
                        return objectMapper.readValue(res.getBody(), new TypeReference<ApiRes<List<GrayTrackDefinition>>>() {
                        });
                    });
                });
    }


    @GetMapping("/service/allInfos")
    public ApiRes<Map<String, List<GrayInstance>>> getAllGrayServiceInfos(
            @RequestParam("serviceId") String serviceId,
            @RequestParam("instanceId") String instanceId) {
        return clientRemoteModule.callClient(serviceId, instanceId,
                "/gray/list/service/allInfos", url -> {
                    return restTemplate.execute(url, HttpMethod.GET, null, res -> {
                        return objectMapper.readValue(res.getBody(), new TypeReference<ApiRes<Map<String, List<GrayInstance>>>>() {
                        });
                    });
                });
    }

    @GetMapping("/infos")
    public ApiRes<Object> getGrayClientInfos(
            @RequestParam("serviceId") String serviceId,
            @RequestParam("instanceId") String instanceId,
            @RequestParam("infoType") String infoType) {
        String uri = infoUrls.get(infoType);
        if (StringUtils.isEmpty(uri)) {
            return ApiResHelper.notFound(String.format("没有找到type为'%s'的info", infoType));
        }
        return clientRemoteModule.callClient(serviceId, instanceId,
                uri, url -> {
                    return restTemplate.execute(url, HttpMethod.GET, null, res -> {
                        return objectMapper.readValue(res.getBody(), new TypeReference<ApiRes<Object>>() {
                        });
                    });
                });
    }


}
