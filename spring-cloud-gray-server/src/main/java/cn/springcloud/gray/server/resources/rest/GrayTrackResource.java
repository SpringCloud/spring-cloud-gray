package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.server.module.GrayServerTrackModule;
import cn.springcloud.gray.server.module.domain.GrayTrack;
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

@RestController
@RequestMapping("/gray/track")
public class GrayTrackResource {

    @Autowired
    private GrayServerTrackModule grayServerTrackModule;

    @RequestMapping(value = "listByInstance", method = RequestMethod.GET, params = "instanceId")
    public List<GrayTrack> listByInstance(@RequestParam("instanceId") String instanceId) {
        return grayServerTrackModule.listGrayTracksByInstanceId(instanceId);
    }


    @GetMapping(value = "/pageByInstance")
    public ResponseEntity<List<GrayTrack>> pageByInstance(
            @RequestParam("instanceId") String instanceId,
            @ApiParam @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GrayTrack> page = grayServerTrackModule.listGrayTracksByInstanceId(instanceId, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(page);
        return new ResponseEntity<List<GrayTrack>>(
                page.getContent(),
                headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "listByService", method = RequestMethod.GET, params = "serviceId")
    public List<GrayTrack> listByService(@RequestParam("serviceId") String serviceId) {
        return grayServerTrackModule.listGrayTracksEmptyInstanceByServiceId(serviceId);
    }


    @GetMapping(value = "/pageByService")
    public ResponseEntity<List<GrayTrack>> pageByService(
            @RequestParam("serviceId") String serviceId,
            @ApiParam @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GrayTrack> page = grayServerTrackModule.listGrayTracksEmptyInstanceByServiceId(serviceId, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(page);
        return new ResponseEntity<List<GrayTrack>>(
                page.getContent(),
                headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        grayServerTrackModule.deleteGrayTrack(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void save(@RequestBody GrayTrack track) {
        grayServerTrackModule.saveGrayTrack(track);
    }
}
