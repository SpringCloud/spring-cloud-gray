package cn.springcloud.gray.service.b.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by saleson on 2017/11/10.
 */
@FeignClient(name = "service-a")
public interface TestClient {

    @RequestMapping(path = "/api/test/get", method = RequestMethod.GET)
    Map<String, String> testGet(@RequestParam(value = "version", required = false) String version);

}
