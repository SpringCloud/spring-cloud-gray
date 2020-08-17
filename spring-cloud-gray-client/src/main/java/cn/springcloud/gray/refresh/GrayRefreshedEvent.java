package cn.springcloud.gray.refresh;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * @author saleson
 * @date 2020-05-05 13:32
 */
@Data
public class GrayRefreshedEvent extends ApplicationEvent {

    private final String triggerName;


    /**
     * Create a new ApplicationEvent.
     *
     * @param triggerName
     * @param source      the object on which the event initially occurred (never {@code null})
     */
    public GrayRefreshedEvent(String triggerName, Object source) {
        super(source);
        this.triggerName = triggerName;
    }


}
