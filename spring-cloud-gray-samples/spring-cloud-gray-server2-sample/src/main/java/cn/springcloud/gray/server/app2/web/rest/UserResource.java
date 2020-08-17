package cn.springcloud.gray.server.app2.web.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.app2.web.domain.fo.LoginFO;
import cn.springcloud.gray.server.app2.web.domain.vo.UserInfoVO;
import cn.springcloud.gray.server.oauth2.Oauth2Service;
import cn.springcloud.gray.server.oauth2.TokenRequestInfo;
import com.google.common.collect.ImmutableBiMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/gray/user")
//@RestController
public class UserResource {

    private static final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private Oauth2Service oauth2Service;

    @PostMapping(value = "/login")
    public ApiRes<Map<String, String>> login(@RequestBody LoginFO fo) {
        if (!StringUtils.equals("admin", fo.getUsername())
                || !StringUtils.equals("abc~!@345", fo.getPassword())) {
            return ApiRes.<Map<String, String>>builder()
                    .code("1")
                    .message("用户名或密码不正确")
                    .build();
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        User user = new User(fo.getUsername(), fo.getPassword(), authorities);
        OAuth2AccessToken oAuth2AccessToken = oauth2Service.getAccessToken(
                TokenRequestInfo.builder().userDetails(user).build());
        return ApiRes.<Map<String, String>>builder()
                .code("0")
                .data(ImmutableBiMap.of("token", oAuth2AccessToken.getValue()))
                .build();
    }

    @RequestMapping(value = "/info", method = {RequestMethod.OPTIONS, RequestMethod.GET})
    public ApiRes<UserInfoVO> info(@RequestParam(required = false, value = "token") String token) {
        log.info("accpect token is {}", token);
        return ApiRes.<UserInfoVO>builder().code("0").data(new UserInfoVO()).build();
    }

    @PostMapping(value = "/logout")
    public ApiRes<Map<String, String>> logout() {
        return ApiRes.<Map<String, String>>builder()
                .code("0")
                .data(ImmutableBiMap.of())
                .build();
    }
}
