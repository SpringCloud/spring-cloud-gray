package cn.springcloud.gray.event.server;

/**
 * 事件触发器
 * 调用事件监听器
 *
 * @author saleson
 * @date 2020-01-30 12:48
 */
public interface GrayEventTrigger extends GrayEventObservable {

    void triggering(Object eventSource, TriggerType triggerType);


}
