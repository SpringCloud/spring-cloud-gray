package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.module.user.domain.UserInfo;
import cn.springcloud.gray.server.module.user.domain.UserQuery;
import cn.springcloud.gray.server.oauth2.Oauth2Service;
import cn.springcloud.gray.server.oauth2.TokenRequestInfo;
import cn.springcloud.gray.server.resources.domain.fo.LoginFO;
import cn.springcloud.gray.server.resources.domain.fo.ResetPasswordFO;
import cn.springcloud.gray.server.resources.domain.fo.UpdatePasswordFO;
import cn.springcloud.gray.server.resources.domain.fo.UserRegisterFO;
import cn.springcloud.gray.server.utils.ApiResHelper;
import cn.springcloud.gray.server.utils.PaginationUtils;
import com.google.common.collect.ImmutableBiMap;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/gray/user")
@RestController
public class UserResource {

    private static final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private Oauth2Service oauth2Service;

    @Autowired
    private UserModule userModule;

    @PostMapping(value = "/login")
    public ApiRes<Map<String, String>> login(@RequestBody LoginFO fo) {
        UserInfo userInfo = userModule.login(fo.getUsername(), fo.getPassword());
        if (userInfo == null) {
            return ApiRes.<Map<String, String>>builder()
                    .code("1")
                    .message("用户名或密码不正确")
                    .build();
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : userInfo.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        User user = new User(fo.getUsername(), fo.getPassword(), authorities);
        OAuth2AccessToken oAuth2AccessToken = oauth2Service.getAccessToken(
                TokenRequestInfo.builder().userDetails(user).build());

        return ApiResHelper.successData(ImmutableBiMap.of("token", oAuth2AccessToken.getValue()));
    }

    @RequestMapping(value = "/info", method = {RequestMethod.OPTIONS, RequestMethod.GET})
    public ApiRes<UserInfo> info() {
        UserInfo userInfo = userModule.getUserInfo(userModule.getCurrentUserId());
        if (userInfo == null) {
            return ApiResHelper.notFound();
        }
        return ApiResHelper.successData(userInfo);
    }

    @PostMapping(value = "/logout")
    public ApiRes<Map<String, String>> logout() {
        return ApiResHelper.successData(ImmutableBiMap.of());
    }


    @GetMapping(value = "/page")
    public ResponseEntity<ApiRes<List<UserInfo>>> page(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "status", required = false) Integer status,
            @ApiParam @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {

        UserQuery userQuery = UserQuery.builder()
                .key(key)
                .status(ObjectUtils.defaultIfNull(status, -1))
                .build();

        Page<UserInfo> userInfoPage = userModule.query(userQuery, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(userInfoPage);
        return new ResponseEntity<>(
                ApiResHelper.successData(userInfoPage.getContent()),
                headers,
                HttpStatus.OK);
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiRes<UserInfo> addUser(@RequestBody UserRegisterFO fo) {
        UserInfo currentUser = userModule.getCurrentUserInfo();
        if (currentUser == null || !currentUser.isAdmin()) {
            return ApiResHelper.notAuthority();
        }
        UserInfo newUserInfo = userModule.register(fo.toUserInfo(), fo.getPassword());
        return ApiResHelper.successData(newUserInfo);
    }


    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ApiRes<UserInfo> updateUser(@RequestBody UserInfo userInfo) {
        UserInfo currentUser = userModule.getCurrentUserInfo();
        if (currentUser == null || !currentUser.isAdmin()) {
            return ApiResHelper.notAuthority();
        }

        userInfo.setOperator(userModule.getCurrentUserId());
        UserInfo newUserInfo = userModule.updateUserInfo(userInfo);
        if (newUserInfo == null) {
            ApiResHelper.notFound();
        }
        return ApiResHelper.successData(newUserInfo);
    }


    @RequestMapping(value = "/resetPassword", method = RequestMethod.PUT)
    public ApiRes<Void> resetPassword(@RequestBody ResetPasswordFO fo) {
        UserInfo currentUser = userModule.getCurrentUserInfo();
        if (currentUser == null || !currentUser.isAdmin()) {
            return ApiResHelper.notAuthority();
        }
        userModule.resetPassword(fo.getUserId(), fo.getPassword());
        return ApiResHelper.success();
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
    public ApiRes<Void> updatePassword(@RequestBody UpdatePasswordFO fo) {
        boolean result = userModule.resetPassword(
                userModule.getCurrentUserId(), fo.getOldPassword(), fo.getNewPassword());

        return result ? ApiResHelper.success() : ApiResHelper.failed();
    }

}
