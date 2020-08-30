package cn.springcloud.gray.refresh;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.config.properties.GrayLoadProperties;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author saleson
 * @date 2020-04-23 23:50
 */
@Slf4j
@Order(100)
public class GrayServiceConfigurationRefresher implements Refresher {


    public static final String GRAY_MANAGER_REFRESH_TRIGGER_NAME = "refresh_gray_manager";

    private GrayLoadProperties grayLoadProperties;
    private GrayManager grayManager;

    public GrayServiceConfigurationRefresher(GrayLoadProperties grayLoadProperties, GrayManager grayManager) {
        this.grayLoadProperties = grayLoadProperties;
        this.grayManager = grayManager;
    }

    @Override
    public boolean refresh() {
        if (!isLoadable()) {
            return false;
        }

        log.info("开始更新灰度实例信息...");
        grayManager.clearAllGrayServices();
        loadProperties();
        log.info("灰度实例信息更新完成。");
        return true;
    }

    @Override
    public boolean load() {
        if (!isLoadable()) {
            return false;
        }

        log.info("开始加载灰度实例信息...");
        loadProperties();
        log.info("灰度实例信息加载完成。");
        return true;
    }


    private void loadProperties() {
        Map<String, GrayLoadProperties.GrayServiceProperties> grayServicePropertiesMap = grayLoadProperties.getServices();
        if (Objects.isNull(grayServicePropertiesMap) || grayServicePropertiesMap.isEmpty()) {
            return;
        }

        grayServicePropertiesMap.forEach((serviceId, serviceProperties) -> {
            loadGrayInstances(serviceId, serviceProperties.getInstances());
        });

        publishRefreshedEvent();
    }

    protected boolean isLoadable() {
        return grayLoadProperties != null && grayLoadProperties.isEnabled();
    }


    private void loadGrayInstances(String serviceId, List<GrayLoadProperties.GrayInstanceProperties> grayInstancePropertiesList) {
        if (Objects.isNull(grayInstancePropertiesList) || grayInstancePropertiesList.isEmpty()) {
            return;
        }
        grayInstancePropertiesList.forEach(grayInstanceProperties -> {
            GrayInstance grayInstance = new GrayInstance();
            grayInstance.setRoutePolicies(new ConcurrentSkipListSet<>(grayInstanceProperties.getRoutePolicies()));
            grayInstance.setServiceId(serviceId);
            grayInstance.setInstanceId(grayInstanceProperties.getInstanceId());
            grayInstance.setGrayStatus(GrayStatus.OPEN);
            grayInstance.setHost(grayInstanceProperties.getHost());
            grayInstance.setPort(grayInstanceProperties.getPort());
            grayManager.updateGrayInstance(grayInstance);
        });
    }

    @Override
    public String triggerName() {
        return GRAY_MANAGER_REFRESH_TRIGGER_NAME;
    }


    private void publishRefreshedEvent() {
        GrayClientHolder.getSpringEventPublisher()
                .publishEvent(new GrayRefreshedEvent(GRAY_MANAGER_REFRESH_TRIGGER_NAME, grayLoadProperties.getServices()));
    }
}
