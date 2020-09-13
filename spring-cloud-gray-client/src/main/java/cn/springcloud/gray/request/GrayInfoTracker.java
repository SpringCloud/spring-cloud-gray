package cn.springcloud.gray.request;

import cn.springcloud.gray.utils.NameUtils;
import org.springframework.core.Ordered;

/**
 * 灰度信息追踪处理器
 *
 * @param <TRACK>
 * @param <REQ>
 */
public interface GrayInfoTracker<TRACK extends GrayTrackInfo, REQ> extends Ordered {

    default boolean shold(TrackArgs<TRACK, REQ> args) {
        return true;
    }

    void call(TrackArgs<TRACK, REQ> args);


    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    default String name() {
        return NameUtils.normalizeName(getClass(), GrayInfoTracker.class);
    }


}
