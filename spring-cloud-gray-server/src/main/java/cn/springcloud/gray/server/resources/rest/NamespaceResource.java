package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.module.NamespaceModule;
import cn.springcloud.gray.server.module.domain.Namespace;
import cn.springcloud.gray.server.module.user.AuthorityModule;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.resources.domain.fo.NamespaceFO;
import cn.springcloud.gray.server.resources.domain.vo.NamespaceOptionVO;
import cn.springcloud.gray.server.resources.domain.vo.NamespaceVO;
import cn.springcloud.gray.server.utils.ApiResHelper;
import cn.springcloud.gray.server.utils.PaginationUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-03-17 12:29
 */
@RestController
@RequestMapping("/namespace")
public class NamespaceResource {

    @Autowired
    private NamespaceModule namespaceModule;
    @Autowired
    private AuthorityModule authorityModule;
    @Autowired
    private UserModule userModule;

    @ApiOperation("获取namespace信息")
    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public ApiRes<Namespace> info(@PathVariable("code") String code) {
        Namespace namespace = namespaceModule.getInfo(code);
        if (Objects.isNull(namespace)) {
            return ApiResHelper.notFound();
        }
        if (!authorityModule.hasNamespaceAuthority(code)) {
            return ApiResHelper.notAuthority();
        }
        return ApiResHelper.successData(namespace);
    }


    @ApiOperation("分页获取namespace信息")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public ApiRes<List<NamespaceOptionVO>> listAll() {
        List<Namespace> namespaces = namespaceModule.listAll(userModule.getCurrentUserId());
        return ApiResHelper.successData(NamespaceOptionVO.of(namespaces));
    }


    @ApiOperation("分页获取namespace信息")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<ApiRes<List<NamespaceVO>>> page(
            @ApiParam @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Namespace> page = namespaceModule.listAll(userModule.getCurrentUserId(), pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(page);
        return new ResponseEntity<>(
                ApiResHelper.successData(toNamespaceVOs(page.getContent(), userModule.getCurrentUserId())),
                headers,
                HttpStatus.OK);
    }


    @ApiOperation("新建namespace")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiRes<NamespaceVO> save(@RequestBody NamespaceFO fo) {
        if (!Objects.isNull(namespaceModule.getInfo(fo.getCode()))) {
            ApiResHelper.failed("资源已存在");
        }
        Namespace namespace = new Namespace();
        namespace.setCode(fo.getCode());
        namespace.setName(fo.getName());
        namespace.setDelFlag(false);
        namespace.setCreator(userModule.getCurrentUserId());
        namespace.setCreateTime(new Date());
        namespace = namespaceModule.addNamespace(namespace);
        return ApiResHelper.successData(toNamespaceVO(namespace, userModule.getCurrentUserId()));
    }


    @ApiOperation("删除namespace")
    @RequestMapping(value = "/{code}", method = RequestMethod.DELETE)
    public ApiRes<Void> delete(@PathVariable("code") String code) {
        if (Objects.isNull(namespaceModule.getInfo(code))) {
            return ApiResHelper.notFound();
        }
        if (!authorityModule.hasNamespaceAuthority(code)) {
            return ApiResHelper.notAuthority();
        }
        if (namespaceModule.deleteNamespace(code)) {
            return ApiResHelper.success();
        }

        return ApiResHelper.failed("删除失败，没有找到记录或该Namespace下仍有资源");
    }


    @ApiOperation("设置默认namespace")
    @RequestMapping(value = "/{code}/default", method = RequestMethod.PUT)
    public ApiRes<Void> setDefault(@PathVariable("code") String code) {
        if (Objects.isNull(namespaceModule.getInfo(code))) {
            return ApiResHelper.notFound();
        }
        if (!authorityModule.hasNamespaceAuthority(code)) {
            return ApiResHelper.notAuthority();
        }
        if (!namespaceModule.setDefaultNamespace(userModule.getCurrentUserId(), code)) {
            return ApiResHelper.failed("操作失败，没有找到记录或该Namespace下仍有资源");
        }
        return ApiResHelper.success();
    }

    @ApiOperation("获取默认namespace")
    @RequestMapping(value = "/default", method = RequestMethod.GET)
    public ApiRes<String> getDefault() {
        String nscode = namespaceModule.getDefaultNamespace(userModule.getCurrentUserId());
        return ApiResHelper.successData(nscode);
    }


    private NamespaceVO toNamespaceVO(Namespace namespace, String userId) {
        NamespaceVO vo = NamespaceVO.of(namespace);
        String defaultCode = namespaceModule.getDefaultNamespace(userId);
        vo.setDefault(StringUtils.equals(defaultCode, vo.getCode()));
        return vo;
    }

    private List<NamespaceVO> toNamespaceVOs(List<Namespace> namespaces, String userId) {
        String defaultCode = namespaceModule.getDefaultNamespace(userId);
        return namespaces.stream().map(namespace -> {
            NamespaceVO vo = NamespaceVO.of(namespace);
            vo.setDefault(StringUtils.equals(defaultCode, vo.getCode()));
            return vo;
        }).collect(Collectors.toList());
    }

}
