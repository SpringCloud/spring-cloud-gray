package cn.springcloud.gray.event.codec;

import cn.springcloud.gray.event.GrayEvent;

import java.io.IOException;

/**
 * @author saleson
 * @date 2020-01-31 22:30
 */
public interface GrayEventDecoder<IN> {

    <T extends GrayEvent> T decode(IN content, Class<T> cls) throws IOException;

}
