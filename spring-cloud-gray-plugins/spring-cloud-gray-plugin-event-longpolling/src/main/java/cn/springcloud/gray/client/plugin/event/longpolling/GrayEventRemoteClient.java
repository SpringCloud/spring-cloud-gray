package cn.springcloud.gray.client.plugin.event.longpolling;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.communication.http.HttpAgent;
import cn.springcloud.gray.http.HttpMethod;
import cn.springcloud.gray.http.HttpParams;
import cn.springcloud.gray.http.HttpRequest;
import cn.springcloud.gray.http.HttpResult;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayTrackDefinition;
import cn.springcloud.gray.event.GrayEvent;
import cn.springcloud.gray.event.GrayEventRetrieveResult;
import cn.springcloud.gray.event.GrayInstanceEvent;
import cn.springcloud.gray.event.GrayTrackEvent;
import cn.springcloud.gray.event.codec.GrayEventDecoder;
import cn.springcloud.gray.event.codec.JsonGrayEventCodec;
import cn.springcloud.gray.event.longpolling.ListenResult;
import cn.springcloud.gray.event.longpolling.domain.fo.LongpollingFO;
import cn.springcloud.gray.event.longpolling.domain.fo.RetrieveFO;
import cn.springcloud.gray.event.longpolling.domain.vo.GrayEventRetrieveVO;
import cn.springcloud.gray.event.longpolling.domain.vo.GrayEventVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-02-04 00:45
 */
@Slf4j
public class GrayEventRemoteClient {

    private static final String PATH_LISTENING_STATUS = "/gray/v2/event/longpolling";
    private static final String PATH_RETRIEVE_NEWEST_EVENTS = "/gray/v2/event/retrieve";

    private HttpAgent httpAgent;
    private ObjectMapper objectMapper;
    private GrayEventDecoder<String> grayEventDecoder;


    public GrayEventRemoteClient(HttpAgent httpAgent) {
        this.httpAgent = httpAgent;
        this.grayEventDecoder = new JsonGrayEventCodec();
        this.objectMapper = new ObjectMapper();
    }

    public ListenResult listeningNewestStatus(LongpollingFO fo) {
        HttpRequest request = new HttpRequest(PATH_LISTENING_STATUS, HttpMethod.GET);
        request.setReadTimeoutMs(fo.getTimeout());

        HttpParams httpParams = new HttpParams();
        httpParams.addParamPair("sortMark", String.valueOf(fo.getSortMark()));
        httpParams.addParamPair("instanceId", fo.getInstanceId());
        httpParams.addParamPair("serviceId", fo.getServiceId());
        httpParams.addParamPair("timeout", String.valueOf(fo.getTimeout()));
        request.setParamValues(httpParams);

        return request(request, new TypeReference<ListenResult>() {
        });

    }

    public GrayEventRetrieveResult retrieveNewestEvents(RetrieveFO fo) {
        HttpRequest request = new HttpRequest(PATH_RETRIEVE_NEWEST_EVENTS, HttpMethod.GET);

        HttpParams httpParams = new HttpParams();
        httpParams.addParamPair("sortMark", String.valueOf(fo.getSortMark()));
        httpParams.addParamPair("instanceId", fo.getInstanceId());
        httpParams.addParamPair("serviceId", fo.getServiceId());
        request.setParamValues(httpParams);

        ApiRes<GrayEventRetrieveVO> apiRes = request(request, new TypeReference<ApiRes<GrayEventRetrieveVO>>() {
        });
        if (Objects.isNull(apiRes)) {
            return null;
        }
        if (!apiRes.judgeSuccess()) {
            log.error("[{}] 请求失败，返回:{} - {}, request:{}", request.getPath(), apiRes.getCode(), apiRes.getMessage(), request);
            return null;
        }

        GrayEventRetrieveVO retrieveVO = apiRes.getData();

        if (Objects.isNull(retrieveVO)) {
            log.error("[{}] 返回结果为空:{}, request:{}", request.getPath(), apiRes, request);
            return null;
        }

        return toGrayEventRetrieveResult(retrieveVO);
    }


    private <T> T request(HttpRequest request, TypeReference<T> typeReference) {
        HttpResult httpResult;
        try {
            httpResult = httpAgent.request(request);
        } catch (IOException e) {
            log.error("[{}] 请求失败:{}", request.getPath(), request, e);
            return null;
        }
        if (StringUtils.isEmpty(httpResult.getContent())) {
            log.error("[{}] 请求失败,参数:{}, 返回:{}", request.getPath(), request, httpResult);
            return null;
        }
        try {
            return objectMapper.readValue(httpResult.getContent(), typeReference);
        } catch (IOException e) {
            log.error("[{}] {} 转{}序列化失败", request.getPath(), httpResult.getContent(), typeReference.getType(), e);
            return null;
        }
    }


    private GrayEventRetrieveResult toGrayEventRetrieveResult(GrayEventRetrieveVO retrieveVO) {
        List<GrayEvent> events = ListUtils.EMPTY_LIST;
        if (CollectionUtils.isNotEmpty(retrieveVO.getGrayEvents())) {
            events = retrieveVO.getGrayEvents()
                    .stream()
                    .map(eventVO -> {
                        try {
                            GrayEvent grayEvent = toGrayEvent(eventVO);
                            return grayEvent;
                        } catch (Exception e) {
                            log.error("[toGrayEvent] 转换失败:{}", eventVO, e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        GrayEventRetrieveResult retrieveResult = new GrayEventRetrieveResult(events);
        if (!Objects.isNull(retrieveVO.getMaxSortMark())) {
            retrieveResult.setMaxSortMark(retrieveVO.getMaxSortMark());
        }
        return retrieveResult;
    }

    private GrayEvent toGrayEvent(GrayEventVO eventVO) throws Exception {
        Class<GrayEvent> cls = (Class<GrayEvent>) Class.forName(eventVO.getEventClass());
        return grayEventDecoder.decode(eventVO.getEvent(), cls);
    }


    public static void main(String[] args) throws JsonProcessingException {
        GrayEventRemoteClient client = new GrayEventRemoteClient(null);
        GrayEventRetrieveVO retrieveVO = new GrayEventRetrieveVO();
        retrieveVO.setMaxSortMark(10l);
        List<GrayEventVO> eventVOS = new ArrayList<>();

        GrayTrackEvent trackEvent = new GrayTrackEvent();
        trackEvent.setServiceId("A");
        trackEvent.setInstanceId("B");
        GrayTrackDefinition trackDefinition = new GrayTrackDefinition();
        trackDefinition.setName("AA");
        trackDefinition.setValue("BB");
        trackEvent.setSource(trackDefinition);

        eventVOS.add(client.toGrayEventVO(trackEvent));

        GrayInstanceEvent instanceEvent = new GrayInstanceEvent();
        instanceEvent.setSortMark(20);
        GrayInstance grayInstance = new GrayInstance();
        grayInstance.setHost("HH");
        instanceEvent.setSource(grayInstance);
        eventVOS.add(client.toGrayEventVO(instanceEvent));

        retrieveVO.setGrayEvents(eventVOS);
        GrayEventRetrieveResult retrieveResult = client.toGrayEventRetrieveResult(retrieveVO);
        System.out.println(retrieveResult);
    }

    private GrayEventVO toGrayEventVO(GrayEvent event) throws JsonProcessingException {
        GrayEventVO vo = new GrayEventVO();
        vo.setEventClass(event.getClass().getName());
        vo.setEvent(objectMapper.writeValueAsString(event));
        return vo;
    }
}
