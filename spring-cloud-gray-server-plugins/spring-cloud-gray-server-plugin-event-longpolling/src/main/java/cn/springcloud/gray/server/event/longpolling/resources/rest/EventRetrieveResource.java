package cn.springcloud.gray.server.event.longpolling.resources.rest;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.event.longpolling.ClientLongPolling;
import cn.springcloud.gray.server.event.longpolling.LongPollingManager;
import cn.springcloud.gray.server.event.longpolling.resources.domain.mapper.GrayEventRetrieveMapper;
import cn.springcloud.gray.server.utils.ApiResHelper;
import cn.springcloud.gray.event.GrayEventRetrieveResult;
import cn.springcloud.gray.event.longpolling.ListenResult;
import cn.springcloud.gray.event.longpolling.domain.fo.LongpollingFO;
import cn.springcloud.gray.event.longpolling.domain.fo.RetrieveFO;
import cn.springcloud.gray.event.longpolling.domain.vo.GrayEventRetrieveVO;
import cn.springcloud.gray.event.server.GrayEventRetriever;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author saleson
 * @date 2020-02-03 21:07
 */
@Api("gray-client调用的接口")
@RestController
@RequestMapping("/gray/v2/event")
public class EventRetrieveResource {

    @Autowired
    private GrayEventRetriever grayEventRetriever;
    @Autowired
    private GrayEventRetrieveMapper grayEventRetrieveMapper;
    @Autowired
    private LongPollingManager longPollingManager;


    @RequestMapping(value = "/retrieve", method = RequestMethod.GET)
    public ApiRes<GrayEventRetrieveVO> retrieve(
            @Validated RetrieveFO fo) {
        GrayEventRetrieveResult retrieveResult = grayEventRetriever.retrieveGreaterThan(fo.getSortMark());
        if (retrieveResult.hasResult()) {
            GrayEventRetrieveVO retrieveVO = grayEventRetrieveMapper.toGrayEventRetrieveVO(retrieveResult);
            return ApiResHelper.successData(retrieveVO);
        }

        return ApiResHelper.notFound("没有找到更新的数据");
    }


    @RequestMapping(value = "/longpolling", method = RequestMethod.GET)
    public DeferredResult<ListenResult> longpolling(
            @Validated LongpollingFO fo) {
        long newestSortMark = grayEventRetriever.getNewestSortMark();
        long timeout = longPollingManager.getTimeout(fo.getTimeout());
        DeferredResult<ListenResult> result = new DeferredResult(timeout);
        if (newestSortMark > fo.getSortMark()) {
            ListenResult listenResult = new ListenResult();
            listenResult.setStatus(ListenResult.RESULT_STATUS_HAS_NEWER);
            result.setResult(listenResult);
            return result;
        }

        ClientLongPolling clientLongPolling = ClientLongPolling.builder()
                .timeout(timeout)
                .instanceId(fo.getInstanceId())
                .serviceId(fo.getServiceId())
                .sortMark(fo.getSortMark())
                .deferredResult(result)
                .build();
        longPollingManager.mount(clientLongPolling);
        return result;
    }


}
