package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.constant.AuthorityConstants;
import cn.springcloud.gray.server.module.domain.OperationAuthrity;
import cn.springcloud.gray.server.module.user.AuthorityModule;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.module.user.domain.AuthorityDetail;
import cn.springcloud.gray.server.module.user.domain.AuthorityQuery;
import cn.springcloud.gray.server.resources.domain.fo.AuthorityDetailFO;
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

@Api
@RestController
@RequestMapping("/authority")
public class AuthorityResource {

    @Autowired
    private AuthorityModule authorityModule;
    @Autowired
    private UserModule userModule;

    @GetMapping(value = "/query")
    public ResponseEntity<ApiRes<List<AuthorityDetail>>> query(
            @RequestParam AuthorityQuery query,
            @ApiParam @PageableDefault(sort = "operateTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AuthorityDetail> page = authorityModule.queryAuthorities(query, pageable);
        return PaginationUtils.generatePaginationResponseResult(page);
    }


    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public ApiRes<Void> delete(
            @ApiParam(name = "id", required = true) @RequestParam("id") Long id) {
        if (!authorityModule.hasAuthorityCurrentUser(AuthorityConstants.RESOURCE_AUTHORITY, OperationAuthrity.DELETE)) {
            return ApiResHelper.notAuthority();
        }
        authorityModule.updateAuthorityDetailDelFlag(id, true, userModule.getCurrentUserId());
        return ApiRes.<Void>builder().code("0").build();
    }

    @RequestMapping(value = "/recover", method = RequestMethod.PATCH)
    public ApiRes<Void> recover(
            @ApiParam(name = "id", required = true) @RequestParam("id") Long id) {
        if (!authorityModule.hasAuthorityCurrentUser(AuthorityConstants.RESOURCE_AUTHORITY, OperationAuthrity.DELETE)) {
            return ApiResHelper.notAuthority();
        }
        authorityModule.updateAuthorityDetailDelFlag(id, false, userModule.getCurrentUserId());
        return ApiRes.<Void>builder().code("0").build();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiRes<AuthorityDetail> save(
            @RequestParam(value = "id", required = false) Long id, @RequestBody AuthorityDetailFO fo) {
        if (!authorityModule.hasAuthorityCurrentUser(AuthorityConstants.RESOURCE_AUTHORITY, OperationAuthrity.EDIT)) {
            return ApiResHelper.notAuthority();
        }

        AuthorityDetail authorityDetail = new AuthorityDetail();
        authorityDetail.setId(id);
        authorityDetail.setAuthorities(fo.getAuthorities());
        authorityDetail.setDelFlag(fo.getDelFlag());
        authorityDetail.setRole(fo.getRole());
        authorityDetail.setResource(fo.getResource());
        authorityDetail.setOperateTime(new Date());
        authorityDetail.setOperator(userModule.getCurrentUserId());
        authorityDetail = authorityModule.saveUserResourceAuthorityDetail(authorityDetail);
        return ApiResHelper.successData(authorityDetail);
    }

}
