package cn.springcloud.gray.web.resources;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.decision.PolicyInfo;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayTrackDefinition;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

/**
 * @author saleson
 * @date 2019-11-24 12:12
 */
@RestController
@RequestMapping("/gray/list")
public class GrayListResource {


    @Autowired
    private GrayTrackHolder grayTrackHolder;
    @Autowired
    private PolicyDecisionManager policyDecisionManager;

    @Autowired
    private GrayManager grayManager;

    /**
     * 返回实例中维护的所有灰度服务及实例信息
     *
     * @return
     */
    @GetMapping("/service/allInfos")
    public Mono<ApiRes<Map<String, Collection<GrayInstance>>>> getAllGrayServiceInfos() {
        return Mono.just(
                ApiRes.<Map<String, Collection<GrayInstance>>>builder()
                        .code(ApiRes.CODE_SUCCESS)
                        .data(grayManager.getMapByAllGrayServices())
                        .build());
    }

    /**
     * 返回维护的所有策略信息
     *
     * @return
     */
    @GetMapping("/policy/allInfos")
    public Mono<ApiRes<Map<String, PolicyInfo>>> getAllPolicyInfos() {
        return Mono.just(
                ApiRes.<Map<String, PolicyInfo>>builder()
                        .code(ApiRes.CODE_SUCCESS)
                        .data(policyDecisionManager.getAllPolicyInfos())
                        .build());
    }

    /**
     * 返回维护的所有灰度追踪信息
     *
     * @return
     */
    @GetMapping("/track/allDefinitions")
    public Mono<ApiRes<Collection<GrayTrackDefinition>>> getAllGrayTracks() {
        return Mono.just(
                ApiRes.<Collection<GrayTrackDefinition>>builder()
                        .code(ApiRes.CODE_SUCCESS)
                        .data(grayTrackHolder.getTrackDefinitions())
                        .build());
    }
}
