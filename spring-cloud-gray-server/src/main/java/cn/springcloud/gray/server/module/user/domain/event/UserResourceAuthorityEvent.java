package cn.springcloud.gray.server.module.user.domain.event;

import cn.springcloud.gray.server.constant.DataOPType;
import cn.springcloud.gray.server.module.user.domain.UserResourceAuthority;
import org.springframework.context.ApplicationEvent;

/**
 * @author saleson
 * @date 2020-08-11 20:09
 */
public class UserResourceAuthorityEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */

    private DataOPType dataOPType;

    public UserResourceAuthorityEvent(DataOPType dataOPType, UserResourceAuthority source) {
        super(source);
        this.dataOPType = dataOPType;
    }


    public UserResourceAuthority getUserResourceAutority() {
        return (UserResourceAuthority) getSource();
    }


    public DataOPType getDataOPType() {
        return dataOPType;
    }
}
