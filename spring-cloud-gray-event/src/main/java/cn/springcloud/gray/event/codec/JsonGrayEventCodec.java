package cn.springcloud.gray.event.codec;

import cn.springcloud.gray.event.GrayEvent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author saleson
 * @date 2020-01-31 22:32
 */
public class JsonGrayEventCodec implements GrayEventCodec<String> {

    private ObjectMapper objectMapper;


    public JsonGrayEventCodec() {
        this(new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
    }

    public JsonGrayEventCodec(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String encode(GrayEvent event) throws IOException {
        return objectMapper.writeValueAsString(event);
    }

    @Override
    public <T extends GrayEvent> T decode(String content, Class<T> cls) throws IOException {
        return objectMapper.readValue(content, cls);
    }
}
