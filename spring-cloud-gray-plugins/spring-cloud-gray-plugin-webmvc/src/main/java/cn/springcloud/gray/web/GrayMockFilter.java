package cn.springcloud.gray.web;

import cn.springcloud.gray.commons.GrayRequestHelper;
import cn.springcloud.gray.mock.GrayReuqestMockInfo;
import cn.springcloud.gray.mock.MockManager;
import cn.springcloud.gray.model.HandleRuleType;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.response.http.HttpResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-27 11:10
 */
@Slf4j
public class GrayMockFilter implements Filter {

    private MockManager mockManager;
    private RequestLocalStorage requestLocalStorage;

    public GrayMockFilter(MockManager mockManager, RequestLocalStorage requestLocalStorage) {
        this.mockManager = mockManager;
        this.requestLocalStorage = requestLocalStorage;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!mockManager.isEnableMock(HandleRuleType.MOCK_APPLICATION_RESPONSE.code())) {
            chain.doFilter(request, response);
            return;
        }

        GrayHttpRequest grayHttpRequest = createGrayHttpRequest((HttpServletRequest) request);
        grayHttpRequest.setGrayTrackInfo(requestLocalStorage.getGrayTrackInfo());

        GrayReuqestMockInfo mockInfo = GrayReuqestMockInfo.builder()
                .grayRequest(grayHttpRequest)
                .build();
        Object mockResult = mockManager.predicateAndExcuteMockHandle(HandleRuleType.MOCK_APPLICATION_RESPONSE.code(), mockInfo);
        if (Objects.isNull(mockResult)) {
            chain.doFilter(request, response);
            return;
        }
        HttpResponseMessage httpResponseMessage = HttpResponseMessage.toHttpResponseMessage(mockResult);
        responseMockMessage((HttpServletResponse) response, httpResponseMessage);

    }

    @Override
    public void destroy() {

    }


    private GrayHttpRequest createGrayHttpRequest(HttpServletRequest request) {
        GrayHttpRequest grayHttpRequest = new GrayHttpRequest();
        GrayRequestHelper.setHttpServerRequestInfoToGrayHttpRequest(request, grayHttpRequest);
        return grayHttpRequest;
    }

    private void responseMockMessage(HttpServletResponse response, HttpResponseMessage httpResponseMessage) {
        response.setStatus(httpResponseMessage.getStatusCode());
        httpResponseMessage.getHeaders().toMap().forEach((k, values) -> {
            String value = CollectionUtils.isEmpty(values) ? "" : StringUtils.join(values, ";");
            response.addHeader(k, value);
        });

        String body = httpResponseMessage.getBodyContent();
        if (Objects.nonNull(body)) {
            try {
                response.getOutputStream().print(body);
            } catch (IOException e) {
                log.error("发送mock数据响应报文发生异常:{}", e.getMessage(), e);
            }
        }
    }

}
