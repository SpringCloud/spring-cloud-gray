package cn.springcloud.gray.event.server;

import cn.springcloud.gray.event.GrayEvent;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-02-05 13:36
 */
public abstract class AbstrctEventConverter<SOURCE, E extends GrayEvent> implements EventConverter<SOURCE, E> {
    @Override
    public E convert(SOURCE source, TriggerType triggerType) {
        E event = null;
        switch (triggerType) {
            case DELETE:
                event = convertDeleteData(source);
                break;
            case MODIFY:
                event = convertModifyData(source);
                break;
            case ADD:
                event = convertAddData(source);
                break;
        }
        if (!Objects.isNull(event)) {
            perfectDefault(event, source, triggerType);
        }
        return event;
    }

    protected void perfectDefault(E event, SOURCE source, TriggerType triggerType) {
        if (Objects.equals(event.getSortMark(), 0l)) {
            event.setSortMark(System.currentTimeMillis());
        }
        if (Objects.isNull(event.getTriggerType())) {
            event.setTriggerType(triggerType);
        }
    }

    protected abstract E convertDeleteData(SOURCE source);

    protected abstract E convertModifyData(SOURCE source);

    protected E convertAddData(SOURCE source) {
        return convertModifyData(source);
    }
}
