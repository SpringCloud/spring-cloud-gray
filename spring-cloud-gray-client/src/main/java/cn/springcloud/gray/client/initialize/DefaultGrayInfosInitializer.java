package cn.springcloud.gray.client.initialize;

import cn.springcloud.gray.GrayClientConfig;
import cn.springcloud.gray.communication.InformationClient;
import cn.springcloud.gray.refresh.RefreshDriver;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 灰度信息初始化
 *
 * @author saleson
 * @date 2020-05-04 23:31
 */
@Slf4j
public class DefaultGrayInfosInitializer implements GrayInfosInitializer {
    private Timer updateTimer = new Timer("GrayInfos-Update-Timer", true);

    private GrayClientConfig grayClientConfig;
    private InformationClient informationClient;
    private RefreshDriver refreshDriver;

    private int scheduleOpenForWorkCount = 0;
    private int scheduleOpenForWorkLimit = 5;


    public DefaultGrayInfosInitializer(
            GrayClientConfig grayClientConfig,
            InformationClient informationClient,
            RefreshDriver refreshDriver) {
        this.grayClientConfig = grayClientConfig;
        this.informationClient = informationClient;
        this.refreshDriver = refreshDriver;
    }

    @Override
    public void setup() {
        scheduleOpenForWork();
    }

    @Override
    public void shutdown() {
        updateTimer.cancel();
    }


    private void scheduleOpenForWork() {
        if (scheduleOpenForWorkCount > scheduleOpenForWorkLimit) {
            return;
        }
        scheduleOpenForWorkCount++;

        updateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                openForWork();
            }
        }, getGrayClientConfig().getInfosInitializeDelayTimeInMs());
    }

    public void openForWork() {
        if (needPullGrayServerInfos()) {
            log.info("拉取灰度信息");
            boolean t = doUpdate();
            int timerMs = getGrayClientConfig().getInfosUpdateIntervalTimerInMs();
            if (timerMs > 0) {
                updateTimer.schedule(new DefaultGrayInfosInitializer.UpdateTask(), timerMs, timerMs);
            } else if (!t) {
                scheduleOpenForWork();
            }
        }
    }


    private boolean doUpdate() {
        try {
            refreshDriver.refresh();
        } catch (Exception e) {
            log.error("更新灰度信息失败", e);
            return false;
        }
        log.error("更新灰度信息成功");
        return true;
    }


    class UpdateTask extends TimerTask {

        @Override
        public void run() {
            doUpdate();
        }
    }

    private GrayClientConfig getGrayClientConfig() {
        return grayClientConfig;
    }

    private boolean needPullGrayServerInfos() {
        return Objects.nonNull(informationClient);
    }
}
