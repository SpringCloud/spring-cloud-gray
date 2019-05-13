package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.server.module.GrayModule;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("gray")
public class GrayResource {

    @Autowired
    private GrayModule grayModule;


    @ApiOperation("返回所有已经打开灰度状态的实例信息（包含决策信息）")
    @RequestMapping(value = "/instance/enable", method = RequestMethod.GET)
    public List<GrayInstance> allOpens() {
        return grayModule.allOpenInstances();
    }

}
