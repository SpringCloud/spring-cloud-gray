package cn.springcloud.gray.event.codec;

import cn.springcloud.gray.event.GrayEvent;

import java.io.IOException;

/**
 * @author saleson
 * @date 2020-01-31 22:29
 */
public interface GrayEventEncoder<OUT> {

    OUT encode(GrayEvent event) throws IOException;

}
