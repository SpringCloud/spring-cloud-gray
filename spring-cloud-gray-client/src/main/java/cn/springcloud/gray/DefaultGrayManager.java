package cn.springcloud.gray;

import cn.springcloud.gray.client.config.properties.GrayLoadProperties;
import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DefaultGrayManager extends AbstractCommunicableGrayManager {

    private Timer updateTimer = new Timer("Gray-Update-Timer", true);
    private GrayLoadProperties grayLoadProperties;

    public DefaultGrayManager(
            GrayClientConfig grayClientConfig,
            GrayLoadProperties grayLoadProperties,
            GrayDecisionFactoryKeeper grayDecisionFactoryKeeper,
            List<RequestInterceptor> requestInterceptors) {
        super(grayClientConfig, grayDecisionFactoryKeeper, requestInterceptors);
        this.grayLoadProperties = grayLoadProperties;


        openForWork();
    }

    public void openForWork() {
        log.info("拉取灰度列表");
        doUpdate();
        int timerMs = getGrayClientConfig().getServiceUpdateIntervalTimerInMs();
        if (timerMs > 0) {
            updateTimer.schedule(
                    new UpdateTask(),
                    getGrayClientConfig().getServiceUpdateIntervalTimerInMs(),
                    getGrayClientConfig().getServiceUpdateIntervalTimerInMs());
        }
    }

    private void doUpdate() {
        try {
            log.debug("更新灰度服务列表...");
            List<GrayInstance> grayInstances = getGrayInformationClient().allGrayInstances();
            Map<String, GrayService> grayServices = new ConcurrentHashMap<>();
            grayInstances.forEach(
                    instance -> {
                        updateGrayInstance(grayServices, instance);
                    });
            joinLoadedGrays(grayServices);
            this.grayServices = grayServices;
        } catch (Exception e) {
            log.error("更新灰度服务列表失败", e);
        }
    }


    /**
     * 加入配置文件中的灰度实例，但不会覆盖列表中的信息
     *
     * @param grayServices 更新的灰度列表
     */
    private void joinLoadedGrays(Map<String, GrayService> grayServices) {
        if (grayLoadProperties != null && grayLoadProperties.isEnabled()) {
            grayLoadProperties.getGrayInstances().forEach(
                    instance -> {
                        if (grayServices.containsKey(instance.getServiceId())
                                || grayServices.get(instance.getServiceId())
                                .getGrayInstance(instance.getInstanceId()) != null) {
                            updateGrayInstance(grayServices, instance);
                        }
                    });
        }
    }

    class UpdateTask extends TimerTask {

        @Override
        public void run() {
            doUpdate();
        }
    }
}
