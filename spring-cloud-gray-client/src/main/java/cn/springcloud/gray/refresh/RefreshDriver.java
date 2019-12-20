package cn.springcloud.gray.refresh;

/**
 * @author saleson
 * @date 2019-12-12 15:53
 */
public interface RefreshDriver {

    void refresh();

    void doRefresh(String triggerName);

}
