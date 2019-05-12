package cn.springcloud.gray;


import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;
import cn.springcloud.gray.decision.factory.GrayDecisionFactory;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DefaultGrayManager extends CommunicableGrayManager {

    private Timer updateTimer = new Timer("Gray-Update-Timer", true);

    public DefaultGrayManager(GrayClientConfig grayClientConfig, GrayDecisionFactoryKeeper grayDecisionFactoryKeeper,
                              List<RequestInterceptor> requestInterceptors) {
        super(grayClientConfig, grayDecisionFactoryKeeper, requestInterceptors);
        openForWork();
    }


    public void openForWork() {
        log.info("拉取灰度列表");
        doUpdate();
        updateTimer.schedule(new UpdateTask(),
                getGrayClientConfig().getServiceUpdateIntervalTimerInMs(),
                getGrayClientConfig().getServiceUpdateIntervalTimerInMs());
    }


    private void doUpdate() {
        try {
            log.debug("更新灰度服务列表...");

            List<GrayInstance> grayInstances = getGrayInformationClient().allGrayInstances();
            Map<String, GrayService> grayServices = new ConcurrentHashMap<>();
            grayInstances.forEach(instance -> {
                updateGrayInstance(grayServices, instance);
            });
            this.grayServices = grayServices;
        } catch (Exception e) {
            log.error("更新灰度服务列表失败", e);
        }
    }


    class UpdateTask extends TimerTask {

        @Override
        public void run() {
            doUpdate();
        }
    }


}
