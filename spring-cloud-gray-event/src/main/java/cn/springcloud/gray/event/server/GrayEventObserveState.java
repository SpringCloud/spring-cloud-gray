package cn.springcloud.gray.event.server;

/**
 * 定义一组GrayEvent可被观察的状态
 *
 * @author saleson
 * @date 2020-08-16 04:09
 */
public enum GrayEventObserveState {
    CREATED, /*READY_FOR_SENDING,*/ SENT
}
