package cn.springcloud.gray.event.sourcehander;

import cn.springcloud.gray.event.GrayEventMsg;

public interface GraySourceEventHandler {

    void handle(GrayEventMsg eventMsg);
}
