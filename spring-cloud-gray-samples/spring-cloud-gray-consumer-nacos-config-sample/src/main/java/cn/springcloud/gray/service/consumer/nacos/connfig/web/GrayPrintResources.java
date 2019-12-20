package cn.springcloud.gray.service.consumer.nacos.connfig.web;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author saleson
 * @date 2019-12-20 22:58
 */
//@RequestMapping("/gray/print")
//@RestController
public class GrayPrintResources {
    @Autowired
    private GrayManager grayManager;
    @Autowired
    private GrayTrackHolder grayTrackHolder;

    @RequestMapping(value = "allServices", method = RequestMethod.GET)
    public Object getAllServices() {
        return grayManager.getMapByAllGrayServices();
    }

    @RequestMapping(value = "getTrackDefinitions", method = RequestMethod.GET)
    public Object getTrackDefinitions() {
        return grayTrackHolder.getTrackDefinitions();
    }
}
