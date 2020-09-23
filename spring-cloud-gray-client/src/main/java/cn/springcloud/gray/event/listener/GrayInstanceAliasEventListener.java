package cn.springcloud.gray.event.listener;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.event.GrayInstanceAliasEvent;

/**
 * @author saleson
 * @date 2020-09-09 00:25
 */
public class GrayInstanceAliasEventListener extends AbstractGrayEventListener<GrayInstanceAliasEvent> {

    private GrayManager grayManager;

    public GrayInstanceAliasEventListener(GrayManager grayManager) {
        this.grayManager = grayManager;
    }

    @Override
    protected void onUpdate(GrayInstanceAliasEvent event) {
        grayManager.setGrayInstanceAlias(event.getSource());
    }

    @Override
    protected void onDelete(GrayInstanceAliasEvent event) {

    }
}
