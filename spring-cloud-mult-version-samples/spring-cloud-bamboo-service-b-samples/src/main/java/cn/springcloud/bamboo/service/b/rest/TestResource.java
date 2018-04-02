package cn.springcloud.bamboo.service.b.rest;

import cn.springcloud.bamboo.service.b.feign.TestClient;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by saleson on 2017/10/18.
 */
@RestController
@RequestMapping("/api/test")
public class TestResource {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TestClient testClient;

    /**
     * test rest template invoke service-a
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/restTemplateGet", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> restTemplateGet(HttpServletRequest request) {
        String url = "http://service-a/api/test/get";
        String query = request.getQueryString();
        if (!StringUtils.isEmpty(query)) {
            url = url + "?" + query;
        }

        Map map = restTemplate.getForObject(url, Map.class);
        return ImmutableMap.of("restTemplateGet", "success.", "service-a-result", map);
    }


    /**
     * test feign invoke service-a
     *
     * @param version
     * @return
     */
    @RequestMapping(value = "/feignGet", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> feignGet(@RequestParam(value = "version", required = false) String version) {
        Map map = testClient.testGet(version);
        return ImmutableMap.of("feignGet", "success.", "service-a-result", map);
    }
}
