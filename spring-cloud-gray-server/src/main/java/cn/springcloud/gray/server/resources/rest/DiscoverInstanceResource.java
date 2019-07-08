package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.model.InstanceInfo;
import cn.springcloud.gray.server.discovery.ServiceDiscovery;
import cn.springcloud.gray.server.module.GrayServerModule;
import cn.springcloud.gray.server.resources.domain.ApiRes;
import cn.springcloud.gray.server.resources.domain.fo.RemoteInstanceStatusUpdateFO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;


/**
 * 用于接收注册中心实例变更信息
 */
@RestController
@RequestMapping("/gray/discover")
public class DiscoverInstanceResource {

    @Autowired
    private GrayServerModule grayServerModule;
    @Autowired
    private ServiceDiscovery serviceDiscovery;
    @Autowired
    private RestTemplate restTemplate;


    /**
     * 接收注册中心实例信息
     *
     * @param instanceInfo 实例信息
     * @return http status
     */
    @RequestMapping(value = "/instanceInfo", method = RequestMethod.POST)
    public ResponseEntity<Void> instanceInfo(@RequestBody InstanceInfo instanceInfo) {
        if (StringUtils.isNotEmpty(instanceInfo.getInstanceId()) && instanceInfo.getInstanceStatus() != null) {
            grayServerModule.updateInstanceStatus(instanceInfo.getInstanceId(), instanceInfo.getInstanceStatus());
        }
        return ResponseEntity.ok().build();
    }


    /**
     * 通知实例修改状态
     *
     * @param instanceStatusUpdateFO 被修改的服务实例信息，以及更新的状态
     * @return http status
     */
    @RequestMapping(value = "/instanceInfo/setInstanceStatus", method = RequestMethod.PUT)
    public ResponseEntity<ApiRes<Void>> setInstanceStatus(@RequestBody RemoteInstanceStatusUpdateFO instanceStatusUpdateFO) {
        InstanceInfo instanceInfo = serviceDiscovery.getInstanceInfo(
                instanceStatusUpdateFO.getServiceId(), instanceStatusUpdateFO.getInstanceId());
        if (instanceInfo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiRes.<Void>builder()
                            .code(ApiRes.CODE_NOT_FOUND)
                            .message(HttpStatus.NOT_FOUND.getReasonPhrase())
                            .build());
        }
        if (StringUtils.isEmpty(instanceInfo.getHost()) || Objects.equals(instanceInfo.getPort(), 0)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiRes.<Void>builder()
                            .code(String.valueOf(HttpStatus.BAD_REQUEST))
                            .message("instance host or port is empty")
                            .build());
        }
        String uri = "/gray/discovery/instance/setStatus?status={status}";
        String url = new StringBuilder("http://").append(instanceInfo.getHost())
                .append(":").append(instanceInfo.getPort())
                .append(uri).toString();
        restTemplate.put(url, null, instanceStatusUpdateFO.getInstanceStatus().name());
        return ResponseEntity.ok(
                ApiRes.<Void>builder()
                        .code(ApiRes.CODE_SUCCESS)
                        .build()
        );
    }

}
