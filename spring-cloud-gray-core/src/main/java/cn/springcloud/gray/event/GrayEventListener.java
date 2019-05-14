package cn.springcloud.gray.event;

import cn.springcloud.gray.exceptions.EventException;

public interface GrayEventListener {


    void onEvent(GrayEventMsg msg) throws EventException;

}
