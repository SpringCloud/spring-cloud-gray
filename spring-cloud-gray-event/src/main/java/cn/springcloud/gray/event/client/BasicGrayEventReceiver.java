package cn.springcloud.gray.event.client;

import cn.springcloud.gray.event.GrayEvent;
import cn.springcloud.gray.event.GrayEventRetrieveResult;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-02-04 14:16
 */
public abstract class BasicGrayEventReceiver implements GrayEventReceiver {


    @Override
    public void receiveRetrieveResult(GrayEventRetrieveResult retrieveResult) {
        processRetrieveResult(retrieveResult);
    }


    protected void processRetrieveResult(GrayEventRetrieveResult retrieveResult) {
        if (!retrieveResult.hasResult()) {
            setLocationNewestSortMark(retrieveResult.getMaxSortMark());
            return;
        }

        retrieveResult.getGrayEvents().forEach(this::invokePublish);
        setLocationNewestSortMark(retrieveResult.getMaxSortMark());
    }

    protected void invokePublish(GrayEvent event) {
        getGrayEventPublisher().publishEvent(event);
    }


    @Override
    public void setLocationNewestSortMark(Long sortMark) {
        if (Objects.isNull(sortMark) || Objects.equals(sortMark, 0)) {
            return;
        }
        updateLocationNewestSortMark(sortMark);
    }

    /**
     * 更新本实例最新的sort mark
     *
     * @param sortMark
     */
    protected abstract void updateLocationNewestSortMark(long sortMark);

}
