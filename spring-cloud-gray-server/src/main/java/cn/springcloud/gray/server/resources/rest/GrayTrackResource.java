package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.module.gray.GrayServerTrackModule;
import cn.springcloud.gray.server.module.gray.domain.GrayTrack;
import cn.springcloud.gray.server.module.user.ServiceManageModule;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.utils.ApiResHelper;
import cn.springcloud.gray.server.utils.PaginationUtils;
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

@RestController
@RequestMapping("/gray/track")
public class GrayTrackResource {

    @Autowired
    private GrayServerTrackModule grayServerTrackModule;
    @Autowired
    private ServiceManageModule serviceManageModule;
    @Autowired
    private UserModule userModule;

    @RequestMapping(value = "listByInstance", method = RequestMethod.GET, params = "instanceId")
    public ApiRes<List<GrayTrack>> listByInstance(@RequestParam("instanceId") String instanceId) {
        return ApiRes.<List<GrayTrack>>builder()
                .code(CODE_SUCCESS)
                .data(grayServerTrackModule.listGrayTracksByInstanceId(instanceId))
                .build();
    }


    @GetMapping(value = "/pageByInstance")
    public ResponseEntity<ApiRes<List<GrayTrack>>> pageByInstance(
            @RequestParam("instanceId") String instanceId,
            @ApiParam @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GrayTrack> page = grayServerTrackModule.listGrayTracksByInstanceId(instanceId, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(page);
        return new ResponseEntity<>(
                ApiRes.<List<GrayTrack>>builder()
                        .code(CODE_SUCCESS)
                        .data(page.getContent())
                        .build(),
                headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "listByService", method = RequestMethod.GET, params = "serviceId")
    public ApiRes<List<GrayTrack>> listByService(@RequestParam("serviceId") String serviceId) {
        return ApiRes.<List<GrayTrack>>builder()
                .code(CODE_SUCCESS)
                .data(grayServerTrackModule.listGrayTracksEmptyInstanceByServiceId(serviceId))
                .build();
    }


    @GetMapping(value = "/pageByService")
    public ResponseEntity<ApiRes<List<GrayTrack>>> pageByService(
            @RequestParam("serviceId") String serviceId,
            @ApiParam @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GrayTrack> page = grayServerTrackModule.listGrayTracks(serviceId, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(page);
        return new ResponseEntity<>(
                ApiRes.<List<GrayTrack>>builder()
                        .code(CODE_SUCCESS)
                        .data(page.getContent())
                        .build(),
                headers,
                HttpStatus.OK);
    }

    @GetMapping(value = "/page")
    public ResponseEntity<ApiRes<List<GrayTrack>>> page(
            @ApiParam @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GrayTrack> page = grayServerTrackModule.listGrayTracks(pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(page);
        return new ResponseEntity<>(
                ApiRes.<List<GrayTrack>>builder()
                        .code(CODE_SUCCESS)
                        .data(page.getContent())
                        .build(),
                headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ApiRes<Void> delete(@PathVariable("id") Long id) {
        GrayTrack grayTrack = grayServerTrackModule.getGrayTrack(id);
        if (grayTrack != null) {
            if (!serviceManageModule.hasServiceAuthority(grayTrack.getServiceId())) {
                return ApiResHelper.notAuthority();
            }
            grayServerTrackModule.deleteGrayTrack(id);
        }
        return ApiRes.<Void>builder()
                .code(CODE_SUCCESS)
                .build();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiRes<GrayTrack> save(@RequestBody GrayTrack track) {
        if (!serviceManageModule.hasServiceAuthority(track.getServiceId())) {
            return ApiResHelper.notAuthority();
        }
        if (Objects.isNull(track.getId())
                && Objects.nonNull(grayServerTrackModule.findFirstGrayTrack(track.getServiceId(), track.getName()))) {
            return ApiResHelper.failed(String.format("service '%s' 已有'%s'类型的追踪", track.getServiceId(), track.getName()));
        }
        track.setOperator(userModule.getCurrentUserId());
        track.setOperateTime(new Date());
        return ApiRes.<GrayTrack>builder()
                .code(CODE_SUCCESS)
                .data(grayServerTrackModule.saveGrayTrack(track))
                .build();
    }
}
