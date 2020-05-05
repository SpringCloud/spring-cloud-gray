package cn.springcloud.gray.refresh;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.client.config.properties.GrayLoadProperties;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author saleson
 * @date 2020-04-24 00:40
 */
@Slf4j
public class GrayTrackConfigurationRefresher implements Refresher {

    public static final String GRAY_TRACK_REFRESH_TRIGGER_NAME = "refresh_gray_track";

    private GrayLoadProperties grayLoadProperties;
    private GrayTrackHolder grayTrackHolder;

    public GrayTrackConfigurationRefresher(GrayLoadProperties grayLoadProperties, GrayTrackHolder grayTrackHolder) {
        this.grayLoadProperties = grayLoadProperties;
        this.grayTrackHolder = grayTrackHolder;
    }

    @Override
    public boolean refresh() {
        if (!isLoadable()) {
            return false;
        }

        log.info("刷新灰度追踪元配置信息");
        grayTrackHolder.clearTrackDefinitions();
        loadProperties();
        return true;
    }

    @Override
    public boolean load() {
        if (!isLoadable()) {
            return false;
        }
        loadProperties();
        return true;
    }


    private void loadProperties() {
        grayLoadProperties.getTrackDefinitions()
                .forEach(grayTrackHolder::updateTrackDefinition);

        publishRefreshedEvent();
    }


    protected boolean isLoadable() {
        return grayLoadProperties != null && grayLoadProperties.isEnabled();
    }

    @Override
    public String triggerName() {
        return GRAY_TRACK_REFRESH_TRIGGER_NAME;
    }


    private void publishRefreshedEvent() {
        GrayClientHolder.getSpringEventPublisher()
                .publishEvent(new GrayRefreshedEvent(GRAY_TRACK_REFRESH_TRIGGER_NAME, grayLoadProperties.getTrackDefinitions()));
    }
}
