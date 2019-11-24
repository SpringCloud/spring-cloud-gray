package cn.springcloud.gray.web.resources;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.model.GrayTrackDefinition;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author saleson
 * @date 2019-11-24 12:12
 */
@RestController
@RequestMapping("/gray/client/track")
public class GrayClientTrackResource {


    @Autowired
    private GrayTrackHolder grayTrackHolder;


    /**
     * 返回维护的所有灰度追踪信息
     *
     * @return
     */
    @GetMapping("/allDefinitions")
    public ApiRes<Collection<GrayTrackDefinition>> getAllGrayTracks() {
        return ApiRes.<Collection<GrayTrackDefinition>>builder()
                .data(grayTrackHolder.getTrackDefinitions())
                .build();
    }
}
