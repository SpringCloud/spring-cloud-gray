package cn.springcloud.gray.refresh;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author saleson
 * @date 2019-12-20 01:52
 */
public class SimpleRefreshDriver implements RefreshDriver {
    private List<Refresher> refreshers;

    public SimpleRefreshDriver(List<Refresher> refreshers) {
        this.refreshers = refreshers;
    }

    @Override
    public void refresh() {
        for (Refresher refresher : refreshers) {
            refresher.refresh();
        }
    }

    @Override
    public void doRefresh(String triggerName) {
        for (Refresher refresher : refreshers) {
            if (StringUtils.equals(triggerName, refresher.triggerName())) {
                refresher.refresh();
            }
        }
    }
}
