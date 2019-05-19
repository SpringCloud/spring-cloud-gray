package cn.springcloud.gray.event;

import cn.springcloud.gray.exceptions.EventException;

/**
 * 灰度事件监听器
 */
public interface GrayEventListener {


    /**
     * 监听方法
     *
     * @param msg 灰度事件
     * @throws EventException 当处理事件出现异常时，抛出异常
     */
    void onEvent(GrayEventMsg msg) throws EventException;

}
