package cn.springcloud.gray.client.initialize;

import org.springframework.context.ApplicationEvent;

/**
 * @author saleson
 * @date 2020-09-10 17:50
 */
public class GrayInitializedEvent extends ApplicationEvent {
    private static final long serialVersionUID = 5958190628849047920L;

    public GrayInitializedEvent() {
        super("GRAY_INITIALIZED");
    }
}
