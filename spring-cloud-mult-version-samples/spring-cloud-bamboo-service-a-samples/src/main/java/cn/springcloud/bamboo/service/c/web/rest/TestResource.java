package cn.springcloud.bamboo.service.a.web.rest;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by saleson on 2017/10/18.
 */
@RestController
@RequestMapping("/api/test")
public class TestResource {
    @Autowired
    Environment env;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> testGet(@RequestParam(value = "version", required = false) String version) {
        return ImmutableMap.of("test", "success.", "version", StringUtils.defaultIfEmpty(version, ""), "serverPort", env.getProperty("server.port"));
    }


    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> testPost(@RequestParam(value = "version", required = false) String version, @RequestBody String body) {
        return ImmutableMap.of(
                "test", "success.", "version", StringUtils.defaultIfEmpty(version, ""),
                "serverPort", env.getProperty("server.port"), "body", body);
    }

}
