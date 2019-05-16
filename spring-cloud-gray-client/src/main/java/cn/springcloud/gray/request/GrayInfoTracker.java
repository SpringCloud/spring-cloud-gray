package cn.springcloud.gray.request;

import cn.springcloud.gray.utils.NameUtils;
import org.springframework.core.Ordered;

public interface GrayInfoTracker<TRACK extends GrayTrackInfo, REQ> extends Ordered {

    void call(TrackArgs<TRACK, REQ> args);


    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    default String name() {
        return NameUtils.normalizeName(getClass(), GrayInfoTracker.class);
    }

    ;

}
