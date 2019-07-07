package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.server.module.GrayServerTrackModule;
import cn.springcloud.gray.server.module.domain.GrayTrack;
import cn.springcloud.gray.server.resources.domain.ApiRes;
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

import java.util.List;

import static cn.springcloud.gray.server.resources.domain.ApiRes.CODE_SUCCESS;

@RestController
@RequestMapping("/gray/track")
public class GrayTrackResource {

    @Autowired
    private GrayServerTrackModule grayServerTrackModule;

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
        grayServerTrackModule.deleteGrayTrack(id);
        return ApiRes.<Void>builder()
                .code(CODE_SUCCESS)
                .build();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiRes<GrayTrack> save(@RequestBody GrayTrack track) {
        return ApiRes.<GrayTrack>builder()
                .code(CODE_SUCCESS)
                .data(grayServerTrackModule.saveGrayTrack(track))
                .build();
    }
}
