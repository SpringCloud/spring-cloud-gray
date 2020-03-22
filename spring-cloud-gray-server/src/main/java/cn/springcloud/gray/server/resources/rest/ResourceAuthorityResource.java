package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.module.domain.ResourceAuthorityFlag;
import cn.springcloud.gray.server.module.user.AuthorityModule;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.module.user.domain.UserResourceAuthority;
import cn.springcloud.gray.server.module.user.domain.UserResourceAuthorityQuery;
import cn.springcloud.gray.server.resources.domain.fo.UserResourceAuthorityFO;
import cn.springcloud.gray.server.utils.ApiResHelper;
import cn.springcloud.gray.server.utils.PaginationUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Api
@RestController
@RequestMapping("/resourceAuthority")
public class ResourceAuthorityResource {

    @Autowired
    private AuthorityModule authorityModule;
    @Autowired
    private UserModule userModule;


    @GetMapping(value = "/query")
    public ResponseEntity<ApiRes<List<UserResourceAuthority>>> query(
            @RequestParam UserResourceAuthorityQuery query,
            @ApiParam @PageableDefault(sort = "operateTime", direction = Sort.Direction.DESC) Pageable pageable) {
//        if (StringUtils.isNotEmpty(query.getResource())
//                && !authorityModule.hasAuthorityCurrentUser(query.getResource())) {
//            return ResponseEntity.ok(ApiResHelper.notAuthority());
//        }
        Page<UserResourceAuthority> page = authorityModule.queryUserResourceAuthority(query, pageable);
        return PaginationUtils.generatePaginationResponseResult(page);
    }


    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public ApiRes<Void> delete(
            @ApiParam(name = "id", required = true) @RequestParam("id") Long id) {
        UserResourceAuthority userResourceAuthority = authorityModule.getUserResourceAuthority(id);
        if (Objects.isNull(userResourceAuthority)) {
            return ApiResHelper.notFound();
        }
        if (!authorityModule.hasResourceAuthority(userResourceAuthority.getResource(),
                userResourceAuthority.getResourceId(), ResourceAuthorityFlag.OWNER)) {
            return ApiResHelper.notAuthority();
        }

        authorityModule.updateUserResourceAuthorityDelFlag(id, true, userModule.getCurrentUserId());
        return ApiRes.<Void>builder().code("0").build();
    }

    @RequestMapping(value = "/recover", method = RequestMethod.PATCH)
    public ApiRes<Void> recover(
            @ApiParam(name = "id", required = true) @RequestParam("id") Long id) {
        UserResourceAuthority userResourceAuthority = authorityModule.getUserResourceAuthority(id);
        if (Objects.isNull(userResourceAuthority)) {
            return ApiResHelper.notFound();
        }
        if (!authorityModule.hasResourceAuthority(userResourceAuthority.getResource(),
                userResourceAuthority.getResourceId(), ResourceAuthorityFlag.OWNER)) {
            return ApiResHelper.notAuthority();
        }
        authorityModule.updateUserResourceAuthorityDelFlag(id, false, userModule.getCurrentUserId());
        return ApiRes.<Void>builder().code("0").build();
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiRes<UserResourceAuthority> save(
            @RequestParam(value = "id", required = false) Long id, @RequestBody UserResourceAuthorityFO fo) {
        if (!authorityModule.hasResourceAuthority(fo.getResource(), fo.getResourceId(), ResourceAuthorityFlag.OWNER)) {
            return ApiResHelper.notAuthority();
        }
        UserResourceAuthority userResourceAuthority = new UserResourceAuthority();
        userResourceAuthority.setId(id);
        userResourceAuthority.setResource(fo.getResource());
        userResourceAuthority.setResourceId(fo.getResourceId());
        userResourceAuthority.setAuthorityFlag(fo.getAuthorityFlag());
        userResourceAuthority.setOperator(userModule.getCurrentUserId());
        userResourceAuthority.setOperateTime(new Date());
        userResourceAuthority.setDelFlag(fo.getDelFlag());
        userResourceAuthority = authorityModule.saveUserResourceAuthorityDetail(userResourceAuthority);
        return ApiResHelper.successData(userResourceAuthority);
    }

}
