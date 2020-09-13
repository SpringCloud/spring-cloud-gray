package cn.springcloud.service.a.rest;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by saleson on 2017/10/18.
 */
@RestController
@RequestMapping("/api/test")
@Slf4j
public class TestResource {
    @Autowired
    Environment env;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> testGet(
            @RequestParam(value = "version", required = false) String version,
            HttpServletRequest request) {
//        Enumeration<String> names = request.getHeaderNames();
//        while (names.hasMoreElements()) {
//            String name = names.nextElement();
//            if (StringUtils.startsWith(name, GrayTrackInfo.GRAY_TRACK_HEADER_PREFIX)) {
//                log.info("{}:{}", name, request.getHeader(name));
//            }
//        }
//        request.getHeader("");
        return ImmutableMap.of("test", "success.", "version", StringUtils.defaultIfEmpty(version, ""), "serverPort", env.getProperty("server.port"));
    }


}
