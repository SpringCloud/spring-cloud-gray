package cn.springcloud.gray.request;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

public interface GrayInfoTracker<TRACK extends GrayTrackInfo, REQ> extends Ordered {

    void call(TRACK trackInfo, REQ request);

    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }


}
