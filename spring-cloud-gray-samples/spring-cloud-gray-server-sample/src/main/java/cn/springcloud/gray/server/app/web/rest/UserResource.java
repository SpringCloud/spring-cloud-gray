package cn.springcloud.gray.server.app.web.rest;

import cn.springcloud.gray.server.app.web.domain.fo.LoginFO;
import cn.springcloud.gray.server.app.web.domain.vo.LoginVO;
import cn.springcloud.gray.server.resources.domain.ApiRes;
import com.google.common.collect.ImmutableBiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/user")
@RestController
public class UserResource {

    private static final Logger log = LoggerFactory.getLogger(UserResource.class);


    @PostMapping(value = "/login")
    public ApiRes<Map<String, String>> login(@RequestBody LoginFO fo) {
        return ApiRes.<Map<String, String>>builder()
                .code("0")
                .data(ImmutableBiMap.of("token", "admin-token"))
                .build();
    }

    @RequestMapping(value = "/info", method = {RequestMethod.OPTIONS, RequestMethod.GET})
    public ApiRes<LoginVO> info(@RequestParam(required = false, value = "token") String token) {
        log.info("accpect token is {}", token);
        return ApiRes.<LoginVO>builder().code("0").data(new LoginVO()).build();
    }
}
