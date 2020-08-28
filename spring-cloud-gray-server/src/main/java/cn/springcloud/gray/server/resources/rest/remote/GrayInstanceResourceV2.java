package cn.springcloud.gray.server.resources.rest.remote;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.server.module.gray.GrayServerModule;
import cn.springcloud.gray.server.module.gray.domain.GrayInstance;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Objects;

import static cn.springcloud.gray.api.ApiRes.CODE_SUCCESS;

@Api("gray-client调用的接口")
@RestController
@RequestMapping("/gray/v2")
public class GrayInstanceResourceV2 {


    @Autowired
    private GrayServerModule grayServerModule;


    @RequestMapping(value = "/instance/", method = RequestMethod.POST)
    public ApiRes<Void> save(@RequestBody GrayInstance grayInstance) {
        if (Objects.isNull(grayInstance.getInstanceStatus())) {
            grayInstance.setInstanceStatus(InstanceStatus.UP);
        }
        grayInstance.setOperateTime(new Date());
        grayServerModule.saveGrayInstance(grayInstance);
        return ApiRes.<Void>builder()
                .code(CODE_SUCCESS)
                .build();
    }


}
