package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.module.HandleRuleModule;
import cn.springcloud.gray.server.module.domain.HandleRule;
import cn.springcloud.gray.server.module.user.AuthorityModule;
import cn.springcloud.gray.server.resources.domain.fo.HandleRuleFO;
import cn.springcloud.gray.server.resources.domain.fo.HandleRuleQueryFO;
import cn.springcloud.gray.server.resources.domain.mapper.HandleRuleVOMapper;
import cn.springcloud.gray.server.resources.domain.vo.HandleRuleVO;
import cn.springcloud.gray.server.utils.ApiResHelper;
import cn.springcloud.gray.server.utils.PaginationUtils;
import cn.springcloud.gray.server.utils.SessionUtils;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static cn.springcloud.gray.api.ApiRes.CODE_SUCCESS;

/**
 * @author saleson
 * @date 2020-05-31 00:37
 */
@RestController
@RequestMapping("/handleRule")
public class HandleRuleResource {

    @Autowired
    private HandleRuleModule handleRuleModule;
    @Autowired
    private HandleRuleVOMapper handleRuleVOMapper;
    @Autowired
    private AuthorityModule authorityModule;


    @GetMapping(value = "/page")
    public ResponseEntity<ApiRes<List<HandleRuleVO>>> page(
            HandleRuleQueryFO queryFO,
            @ApiParam @PageableDefault(sort = "order", direction = Sort.Direction.DESC) Pageable pageable) {
        if (cn.springcloud.gray.utils.StringUtils.isEmpty(queryFO.getNamespace())) {
            return ResponseEntity.ok(ApiResHelper.failed("namespace 不能为空"));
        }
        if (!authorityModule.hasNamespaceAuthority(queryFO.getNamespace())) {
            return ResponseEntity.ok(ApiResHelper.notAuthority());
        }
        Page<HandleRule> page = handleRuleModule.queryHandleRules(queryFO.toHandleRuleQuery(), pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(page);
        return new ResponseEntity<>(
                ApiRes.<List<HandleRuleVO>>builder()
                        .code(CODE_SUCCESS)
                        .data(handleRuleVOMapper.toVOs(page.getContent()))
                        .build(),
                headers,
                HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ApiRes<Void> delete(@PathVariable("id") Long id) {
        HandleRule handleRule = handleRuleModule.getHandleRule(id);
        if (Objects.isNull(handleRule)) {
            return ApiResHelper.notFound();
        }
        if (!authorityModule.hasNamespaceAuthority(handleRule.getNamespace())) {
            return ApiResHelper.notAuthority();
        }
        handleRuleModule.deleteHandleRule(id, SessionUtils.currentUserId());
        return ApiRes.<Void>builder().code(CODE_SUCCESS).build();
    }

    @RequestMapping(value = "/{id}/recover", method = RequestMethod.PATCH)
    public ApiRes<Void> recover(@PathVariable("id") Long id) {
        HandleRule handleRule = handleRuleModule.getHandleRule(id);
        if (Objects.isNull(handleRule)) {
            return ApiResHelper.notFound();
        }
        if (!authorityModule.hasNamespaceAuthority(handleRule.getNamespace())) {
            return ApiResHelper.notAuthority();
        }
        handleRuleModule.recoverHandleRule(id, SessionUtils.currentUserId());
        return ApiRes.<Void>builder().code(CODE_SUCCESS).build();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiRes<HandleRuleVO> save(@RequestBody HandleRuleFO handleRuleFO) {
        HandleRule handleRule = handleRuleModule.getHandleRule(handleRuleFO.getId());
        if (Objects.isNull(handleRule)) {
            handleRule = new HandleRule();
            handleRule.setId(handleRuleFO.getId());
            handleRule.setNamespace(handleRuleFO.getNamespace());
            handleRule.setDelFlag(false);
        }
        if (!authorityModule.hasNamespaceAuthority(handleRule.getNamespace())) {
            return ApiResHelper.notAuthority();
        }
        handleRuleFO.fillToHandleRule(handleRule);
        handleRule.setOperator(SessionUtils.currentUserId());
        handleRule.setOperateTime(new Date());

        return ApiRes.<HandleRuleVO>builder()
                .code(CODE_SUCCESS)
                .data(handleRuleVOMapper.toVO(handleRuleModule.saveHandleRule(handleRule)))
                .build();
    }

}
