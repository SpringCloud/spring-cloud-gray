package cn.springcloud.gray.server.resources.rest.remote;

import cn.springcloud.gray.server.module.gray.GrayModule;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("gray-client调用的接口")
@RestController
@RequestMapping("/gray/v2")
public class GrayResourceV2 {


    @Autowired
    private GrayModule grayModule;
    

}
