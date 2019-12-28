package cn.springcloud.gray.dynamic.decision;

import cn.springcloud.gray.CachedGrayManager;
import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.dynamiclogic.DynamicLogicEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author saleson
 * @date 2019-12-28 10:04
 */
public class DynamicDecisionListener implements ApplicationListener<DynamicLogicEvent> {

    private GrayManager grayManager;

    public DynamicDecisionListener(GrayManager grayManager) {
        this.grayManager = grayManager;
    }

    @Override
    public void onApplicationEvent(DynamicLogicEvent event) {
        if (grayManager instanceof CachedGrayManager) {
            CachedGrayManager cachedGrayManager = (CachedGrayManager) grayManager;
            cachedGrayManager.getGrayDecisionCache().invalidateAll();
        }
    }
}
