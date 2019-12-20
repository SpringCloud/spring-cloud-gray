package cn.springcloud.gray.plugin.refresher;

import cn.springcloud.gray.DefaultGrayManager;
import cn.springcloud.gray.refresh.RefreshDriver;
import cn.springcloud.gray.request.track.DefaultGrayTrackHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author saleson
 * @date 2019-12-20 13:00
 */
public class EnvironmentChangeListener implements ApplicationListener<EnvironmentChangeEvent> {

    private RefreshDriver refreshDriver;
    private List<TriggerItem> triggerItems = new ArrayList<>();
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);


    public EnvironmentChangeListener(RefreshDriver refreshDriver) {
        this.refreshDriver = refreshDriver;
        init();
    }


    private void init() {
        triggerItems.add(new TriggerItem(
                DefaultGrayManager.GRAY_MANAGER_REFRESH_TRIGGER_NAME,
                StringMatchers.prefixsAnyMatcher(
                        "gray.load.grayInstances.", "gray.load.gray-instances.",
                        "gray.load.grayInstances[", "gray.load.gray-instances[")));

        triggerItems.add(new TriggerItem(
                DefaultGrayTrackHolder.GRAY_TRACK_REFRESH_TRIGGER_NAME,
                StringMatchers.prefixsAnyMatcher(
                        "gray.request.track.web.trackDefinitions.", "gray.request.track.web.track-definitions.",
                        "gray.request.track.web.trackDefinitions[", "gray.request.track.web.track-definitions[")));
    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        getTriggerNames(event.getKeys()).forEach(this::invokeRefresher);
    }


    private void invokeRefresher(String triggerName) {
        scheduledExecutorService.schedule(() -> refreshDriver.doRefresh(triggerName), 30, TimeUnit.MILLISECONDS);
    }

    private Set<String> getTriggerNames(Set<String> keys) {
        Set<String> triggerNames = new HashSet<>();
        triggerItems.forEach(item -> {
            if (item.matching(keys)) {
                triggerNames.add(item.getTriggerName());
            }
        });
        return triggerNames;
    }


    @Data
    @AllArgsConstructor
    private class TriggerItem {
        private String triggerName;
        private StringMatcher stringMatcher;

        public boolean matching(Set<String> keys) {
            for (String key : keys) {
                if (stringMatcher.matching(key)) {
                    return true;
                }
            }
            return false;
        }

    }
}
