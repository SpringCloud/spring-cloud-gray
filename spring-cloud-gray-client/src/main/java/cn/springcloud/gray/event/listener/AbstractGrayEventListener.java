package cn.springcloud.gray.event.listener;

import cn.springcloud.gray.event.GrayEvent;
import cn.springcloud.gray.event.client.GrayEventListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author saleson
 * @date 2020-02-03 19:00
 */
@Slf4j
public abstract class AbstractGrayEventListener<E extends GrayEvent> implements GrayEventListener<E> {
    @Override
    public void onEvent(E event) {
        printEvent("成功接收事件", event);
        if (!validate(event)) {
            printEvent("事件校验失败", event);
            return;
        }

        switch (event.getTriggerType()) {
            case ADD:
            case MODIFY:
                onUpdate(event);
                printEvent("更新事件执行成功", event);
                break;
            case DELETE:
                onDelete(event);
                printEvent("删除事件执行成功", event);
                break;
        }
    }

    protected abstract void onUpdate(E event);

    protected abstract void onDelete(E event);

    protected boolean validate(E event) {
        return true;
    }

    protected void printEvent(String msg, E event) {
        log.info(msg + "[{}] {} - {} : {}", event.getTriggerType(), event.getType(), event.getSourceId(), event);
    }
}
