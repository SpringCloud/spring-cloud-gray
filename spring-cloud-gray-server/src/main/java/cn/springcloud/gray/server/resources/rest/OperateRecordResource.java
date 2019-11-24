package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.module.audit.OperateAuditModule;
import cn.springcloud.gray.server.module.audit.domain.OperateQuery;
import cn.springcloud.gray.server.module.audit.domain.OperateRecord;
import cn.springcloud.gray.server.resources.domain.fo.OperateQueryFO;
import cn.springcloud.gray.server.utils.PaginationUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
@RequestMapping("/gray/operate/record")
public class OperateRecordResource {

    @Autowired
    private OperateAuditModule operateAuditModule;


//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "operator", value = "操作人id", dataType = "string"),
//            @ApiImplicitParam(name = "operateState", value = "操作结果,{-1:all, 0:failed, 1: scuuessed}", dataType = "int",
//                    defaultValue = "0", allowableValues = "-1,0,1")
//    })
//    @GetMapping(value = "/page")
//    public ResponseEntity<ApiRes<List<OperateRecord>>> list(
//            @RequestParam(value = "operator", required = false) String operator,
//            @RequestParam(value = "ip", required = false) String ip,
//            @RequestParam(value = "uri", required = false) String uri,
//            @RequestParam(value = "handler", required = false) String handler,
//            @RequestParam(value = "apiResCode", required = false) String apiResCode,
//            @RequestParam(value = "operateState", required = false) int operateState,
//            @RequestParam(value = "startTime", required = false) Date startTime,
//            @RequestParam(value = "endTime", required = false) Date endTime,
//            @ApiParam @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
//        OperateQuery query = OperateQuery.builder()
//                .apiResCode(apiResCode)
//                .operateEndTime(endTime)
//                .operateStartTime(startTime)
//                .ip(ip)
//                .uri(uri)
//                .handler(handler)
//                .operator(operator)
//                .operateState(operateState == -1 ? null : operateState)
//                .build();
//        Page<OperateRecord> operateRecordPage = operateAuditModule.queryRecords(query, pageable);
//        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(operateRecordPage);
//        ApiRes<List<OperateRecord>> data = ApiRes.<List<OperateRecord>>builder()
//                .code(ApiRes.CODE_SUCCESS)
//                .data(operateRecordPage.getContent())
//                .build();
//        return new ResponseEntity<>(
//                data,
//                headers,
//                HttpStatus.OK);
//    }

    @GetMapping(value = "/page")
    public ResponseEntity<ApiRes<List<OperateRecord>>> list(
            @Validated OperateQueryFO fo,
            @ApiParam @PageableDefault(sort = "operateTime", direction = Sort.Direction.DESC) Pageable pageable) {
        OperateQuery query = fo.toOperateQuery();
        Page<OperateRecord> operateRecordPage = operateAuditModule.queryRecords(query, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(operateRecordPage);
        ApiRes<List<OperateRecord>> data = ApiRes.<List<OperateRecord>>builder()
                .code(ApiRes.CODE_SUCCESS)
                .data(operateRecordPage.getContent())
                .build();
        return new ResponseEntity<>(
                data,
                headers,
                HttpStatus.OK);
    }


}
