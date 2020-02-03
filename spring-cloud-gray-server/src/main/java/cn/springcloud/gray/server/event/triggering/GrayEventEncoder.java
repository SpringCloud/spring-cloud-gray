package cn.springcloud.gray.server.event.triggering;

import cn.springlcoud.gray.event.GrayEvent;

import java.io.IOException;

/**
 * @author saleson
 * @date 2020-01-31 22:29
 */
public interface GrayEventEncoder<OUT> {

    OUT encode(GrayEvent event) throws IOException;

}
