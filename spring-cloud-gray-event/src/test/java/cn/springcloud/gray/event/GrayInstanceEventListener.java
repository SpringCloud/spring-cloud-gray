package cn.springcloud.gray.event;

import cn.springcloud.gray.event.client.GrayEventListener;

/**
 * @author saleson
 * @date 2020-01-30 21:42
 */
public class GrayInstanceEventListener implements GrayEventListener<TGrayInstanceEvent> {
    @Override
    public void onEvent(TGrayInstanceEvent event) {
        GrayEventPublisherTest.testList.add(event);
    }
}
