package cn.springcloud.gray.event;

import cn.springcloud.gray.event.client.GrayEventListener;

/**
 * @author saleson
 * @date 2020-01-30 21:42
 */
public class GrayPolicyEventListener implements GrayEventListener<TGrayPolicyEvent> {
    @Override
    public void onEvent(TGrayPolicyEvent event) {
        GrayEventPublisherTest.testList.add(event);
    }
}
