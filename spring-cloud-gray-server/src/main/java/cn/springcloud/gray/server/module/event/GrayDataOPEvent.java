package cn.springcloud.gray.server.module.event;

import cn.springcloud.gray.server.constant.DataOPType;
import org.springframework.context.ApplicationEvent;

/**
 * @author saleson
 * @date 2020-09-18 00:54
 */
public class GrayDataOPEvent<S> extends ApplicationEvent {

    private static final long serialVersionUID = -1565403107051440961L;
    private DataOPType dataOPType;

    public GrayDataOPEvent(DataOPType dataOPType, S s) {
        super(s);
        this.dataOPType = dataOPType;
    }

    public DataOPType getDataOPType() {
        return dataOPType;
    }


    public S getEventSource() {
        return (S) getSource();
    }
}
