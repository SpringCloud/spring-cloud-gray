package cn.springcloud.gray.communication;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.communication.exception.CommunicationException;
import cn.springcloud.gray.communication.http.HttpAgent;
import cn.springcloud.gray.http.HttpMethod;
import cn.springcloud.gray.http.HttpRequest;
import cn.springcloud.gray.http.HttpResult;
import cn.springcloud.gray.model.GrayInfos;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayTrackDefinition;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class HttpInformationClient implements InformationClient {
    private static final Logger log = LoggerFactory.getLogger(HttpInformationClient.class);
    private static final String BASE_PATH = "/gray/v2";
    private HttpAgent httpAgent;
    private ObjectMapper objectMapper;

    public HttpInformationClient(HttpAgent httpAgent) {
        this(httpAgent, new ObjectMapper());
    }

    public HttpInformationClient(HttpAgent httpAgent, ObjectMapper objectMapper) {
        this.httpAgent = httpAgent;
        this.objectMapper = objectMapper;
    }

    @Override
    public GrayInfos allInfos(String serviceId, String instanceId) {
        HttpRequest request = new HttpRequest(getFullPath("/all"), HttpMethod.GET);
        request.initHttpParams()
                .addParamPair("serviceId", serviceId)
                .addParamPair("instanceId", instanceId);
        TypeReference<ApiRes<GrayInfos>> typeReference = new TypeReference<ApiRes<GrayInfos>>() {
        };
        return requestData("获取所有灰度信息", request, typeReference);
    }

    @Override
    public List<GrayInstance> allGrayInstances() {
        HttpRequest request = new HttpRequest(getFullPath("/instances/enable"), HttpMethod.GET);
        TypeReference<ApiRes<List<GrayInstance>>> typeReference = new TypeReference<ApiRes<List<GrayInstance>>>() {
        };
        return requestData("获取灰度实例列表", request, typeReference);
    }


    @Override
    public void addGrayInstance(GrayInstance grayInstance) {
        HttpRequest request = new HttpRequest("/gray/v2/instance/", HttpMethod.POST);
        request.initHttpHeaders()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        try {
            String json = objectMapper.writeValueAsString(grayInstance);
            request.setBody(json);
        } catch (JsonProcessingException e) {
            throw new CommunicationException("参数json序列化失败", e);
        }
        TypeReference<ApiRes<Void>> typeReference = new TypeReference<ApiRes<Void>>() {
        };
        requestData("注册灰度实例", request, typeReference);
    }

    @Override
    public GrayInstance getGrayInstance(String serviceId, String instanceId) {
        HttpRequest request = new HttpRequest(getFullPath("/instance"), HttpMethod.GET);
        request.initHttpParams()
                .addParamPair("serviceId", serviceId)
                .addParamPair("instanceId", instanceId);
        TypeReference<ApiRes<GrayInstance>> typeReference = new TypeReference<ApiRes<GrayInstance>>() {
        };
        return requestData("获取灰度实例", request, typeReference);
    }

    @Override
    public void serviceDownline(String instanceId) {
        String path = "/gray/instance/" + instanceId + "/switchStatus";
        HttpRequest request = new HttpRequest(path, HttpMethod.PUT);
        request.initHttpParams()
                .addParamPair("switch", "0");
        TypeReference<ApiRes<Void>> typeReference = new TypeReference<ApiRes<Void>>() {
        };
        requestData("灰度服务实例下线", request, typeReference);
    }

    @Override
    public List<GrayTrackDefinition> getTrackDefinitions(String serviceId, String instanceId) {
        HttpRequest request = new HttpRequest(getFullPath("/instance"), HttpMethod.GET);
        request.initHttpParams()
                .addParamPair("serviceId", serviceId)
                .addParamPair("instanceId", instanceId);
        TypeReference<ApiRes<List<GrayTrackDefinition>>> typeReference = new TypeReference<ApiRes<List<GrayTrackDefinition>>>() {
        };
        return requestData("获取灰度追踪信息", request, typeReference);
    }


    protected String getFullPath(String path) {
        return BASE_PATH + path;
    }


    private HttpResult request(HttpRequest httpRequest) {
        HttpResult httpResult = null;
        try {
            httpResult = httpAgent.request(httpRequest);
        } catch (IOException e) {
            throw new CommunicationException("", e);
        }
        if (!Objects.equals(httpResult.getCode(), HttpStatus.OK.value())) {
            log.error("请求 {} 返回 {} -> {}",
                    httpRequest.getPath(), httpResult.getCode(), httpResult.getContent());
            throw new CommunicationException("接口返回状态码异常: " + httpResult.getCode());
        }
        return httpResult;
    }

    private <T> T requestData(String action, HttpRequest httpRequest, TypeReference<ApiRes<T>> typeReference) {
        try {
            HttpResult httpResult = request(httpRequest);
            return parseApiDate(httpResult.getContent(), typeReference);
        } catch (RuntimeException e) {
            Throwable cause = e;
            if (e instanceof CommunicationException && Objects.nonNull(e.getCause())) {
                cause = e.getCause();
            }
            log.error("{}失败:{}", action, e.getMessage(), cause);
            throw e;
        }
    }


    protected <T> ApiRes<T> parseApiResult(String json, TypeReference<ApiRes<T>> referenceType) {
        try {
            return objectMapper.readValue(json, referenceType);
        } catch (IOException e) {
            log.error("返回的数据格式异常, 需要{}格式, 返回的数据:{}", referenceType.getType(), json);
            throw new CommunicationException("返回的数据格式异常", e);
        }
    }

    protected <T> T parseApiDate(String json, TypeReference<ApiRes<T>> typeReference) {
        ApiRes<T> apiRes = parseApiResult(json, typeReference);
        if (!apiRes.judgeSuccess()) {
            log.error("接口数据异常， code:{}, msg:{}", apiRes.getCode(), apiRes.getMessage());
            throw new CommunicationException("接口异常, api code:" + apiRes.getCode() + ", msg:" + apiRes.getMessage());
        }
        return apiRes.getData();
    }
}
