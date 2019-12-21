package cn.springcloud.gray.apollo.sample.core;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author saleson
 * @date 2019-12-21 21:35
 */
public class EnvironmentRefresher implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(EnvironmentRefresher.class);
    private static final String KEY_REFRESH_MODE = "apollo.refresh.mode";

    private volatile RefreshMode refreshMode = null;

    private Map<RefreshMode, Consumer<ConfigChangeEvent>> refreshHandlers = new ConcurrentHashMap<>();

    private ApplicationContext applicationContext;
    private ContextRefresher contextRefresher;


    public EnvironmentRefresher() {
        init();
    }

    public void refresh(ConfigChangeEvent changeEvent) {
        RefreshMode refreshMode = getRefreshMode(changeEvent);
        Optional.ofNullable(refreshHandlers.get(refreshMode)).orElse(this::refreshAll).accept(changeEvent);
    }

    private void init() {
        refreshHandlers.put(RefreshMode.ALL, this::refreshAll);
        refreshHandlers.put(RefreshMode.PROPERTIES, this::refreshProperties);
    }

    private void refreshProperties(ConfigChangeEvent changeEvent) {
        applicationContext.publishEvent(new EnvironmentChangeEvent(applicationContext, changeEvent.changedKeys()));
    }

    private void refreshAll(ConfigChangeEvent changeEvent) {
        ContextRefresher contextRefresher = getContextRefresher();
        if (contextRefresher != null) {
            contextRefresher.refresh();
            return;
        }
        refreshProperties(changeEvent);
        RefreshScope refreshScope = applicationContext.getBean(RefreshScope.class);
        if (refreshScope != null) {
            refreshScope.refreshAll();
        }
    }

    private ContextRefresher getContextRefresher() {
        if (contextRefresher == null) {
            try {
                contextRefresher = applicationContext.getBean(ContextRefresher.class);
            } catch (Exception e) {
                log.error("获取ContextRefresher实例失败", e);
            }
        }
        return contextRefresher;
    }

    private RefreshMode getRefreshMode(ConfigChangeEvent changeEvent) {

        if (!changeEvent.changedKeys().contains(KEY_REFRESH_MODE)) {
            return getRefreshMode();
        }

        String refreshModeStr = changeEvent.getChange(KEY_REFRESH_MODE).getNewValue();

        RefreshMode refreshMode = covertRefreshMode(refreshModeStr);

        if (Objects.nonNull(refreshMode)) {
            this.refreshMode = refreshMode;
            return this.refreshMode;
        }
        return getRefreshMode();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    private RefreshMode getRefreshMode() {

        if (Objects.isNull(refreshMode)) {
            refreshMode = covertRefreshMode(applicationContext.getEnvironment().getProperty(KEY_REFRESH_MODE), RefreshMode.ALL);
        }
        return refreshMode;
    }

    /**
     * String 转 RefreshMode
     *
     * @return
     */
    private RefreshMode covertRefreshMode(String refreshModeStr) {
        return covertRefreshMode(refreshModeStr, null);
    }

    /**
     * String 转 RefreshMode
     *
     * @return
     */
    private RefreshMode covertRefreshMode(String refreshModeStr, RefreshMode defaultRefreshMode) {

        if (StringUtils.isBlank(refreshModeStr)) {
            return defaultRefreshMode;
        }
        try {
            return RefreshMode.valueOf(refreshModeStr.toUpperCase());
        } catch (Exception e) {
            log.warn("covertRefreshMode[{}] RefreshMode转换失败", refreshModeStr);
            return defaultRefreshMode;
        }

    }

    /**
     * 刷新模式
     */
    public enum RefreshMode {
        ALL, PROPERTIES


    }

}