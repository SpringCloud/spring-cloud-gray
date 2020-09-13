package cn.springcloud.gray.service.b.rest;

import cn.springcloud.gray.client.config.properties.GrayProperties;
import cn.springcloud.gray.service.b.feign.Test2Client;
import cn.springcloud.gray.service.b.feign.TestClient;
import cn.springcloud.gray.service.b.service.TestService;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by saleson on 2017/10/18.
 */
@RestController
@RequestMapping("/api/test")
@Slf4j
public class TestResource {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TestClient testClient;
    @Autowired
    private Test2Client test2Client;
    @Autowired
    private TestService testService;
    @Autowired
    private GrayProperties grayProperties;


    @RequestMapping(value = "/testGet", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> testGet(HttpServletRequest request) {
        return ImmutableMap.of("restTemplateGet", "success.", "service-b-result", "");
    }


    /**
     * test rest template invoke service-a
     *
     * @param request HttpServletRequest
     * @return 内容
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
     * @param version 请求的版本
     * @param request 请求
     * @return 内容
     */
    @RequestMapping(value = "/feignGet", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> feignGet(
            @RequestParam(value = "version", required = false) String version,
            HttpServletRequest request) {
//        Enumeration<String> names = request.getHeaderNames();
//        while (names.hasMoreElements()) {
//            String name = names.nextElement();
////            if (org.apache.commons.lang.StringUtils.startsWith(name, GrayTrackInfo.GRAY_TRACK_HEADER_PREFIX)) {
//            log.info("{}:{}", name, request.getHeader(name));
//            Enumeration<String> values = request.getHeaders(name);
//            while (values.hasMoreElements()) {
//                System.out.println(values.nextElement());
//            }
////            }
//        }
        Map map = testClient.testGet(version);
        return ImmutableMap.of("feignGet", "success.", "service-a-result", map);
    }

    @RequestMapping(value = "/feignGetAsync", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> feignGetAsync(
            @RequestParam(value = "version", required = false) String version,
            HttpServletRequest request) throws ExecutionException, InterruptedException {
        Future<Map> map = testService.get(version);
        return ImmutableMap.of("feignGet", "success.", "service-a-result", map.get());
    }

    @RequestMapping(value = "/feign2Get", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> feign2Get(
            @RequestParam(value = "version", required = false) String version,
            HttpServletRequest request) {
        Map map = test2Client.testGet(version);
        return ImmutableMap.of("feignGet", "success.", "service-a-result", map);
    }


    @RequestMapping(value = "/graySwith", method = RequestMethod.GET)
    @ResponseBody
    public String graySwith() {
        grayProperties.setEnabled(!grayProperties.isEnabled());
        return "sccuess";
    }
}
