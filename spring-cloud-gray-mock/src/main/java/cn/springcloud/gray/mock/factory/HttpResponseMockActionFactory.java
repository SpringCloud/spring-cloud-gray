package cn.springcloud.gray.mock.factory;

import cn.springcloud.gray.component.bean.binder.BeanBinder;
import cn.springcloud.gray.component.bean.binder.MapSpringBeanBinder;
import cn.springcloud.gray.mock.MockAction;
import cn.springcloud.gray.response.http.HttpResponseMessage;
import cn.springcloud.gray.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-21 00:15
 */
@Slf4j
public class HttpResponseMockActionFactory extends AbstractMockActionFactory<HttpResponseMockActionFactory.Config, Map> {

    private ObjectMapper objectMapper = new ObjectMapper();

    public HttpResponseMockActionFactory() {
        this(new MapSpringBeanBinder(new SpelExpressionParser(), DefaultConversionService.getSharedInstance()));
    }

    public HttpResponseMockActionFactory(BeanBinder<Map> beanBinder) {
        super(beanBinder);
    }

    @Override
    public MockAction<HttpResponseMessage<String>> apply(Config config) {
        return info -> {
            HttpResponseMessage<String> responseMessage = new HttpResponseMessage<>();
            String body = config.getBody();
//            Map<String, String> map = BeanUtils.toStringMap(info);
//            if (config.isReplace()) {
//                StringUtils.replacAll();
//            }
            responseMessage.setBody(config.getBody());
            if (StringUtils.isEmpty(config.getHeaders())) {
                return responseMessage;
            }

            Map<String, String> headers = null;
            try {
                headers = objectMapper.readValue(config.getHeaders(), new TypeReference<Map<String, String>>() {

                });
            } catch (IOException e) {
                log.error("解析json失败, 内容: {}", config.getHeaders(), e);
            }

            if (Objects.isNull(headers) || headers.isEmpty()) {
                return responseMessage;
            }

            headers.forEach(responseMessage.getHeaders()::addHeader);
            return responseMessage;
        };
    }

    @Data
    public static class Config {
        private String headers;
        private String body;
        private boolean replace;
    }
}
