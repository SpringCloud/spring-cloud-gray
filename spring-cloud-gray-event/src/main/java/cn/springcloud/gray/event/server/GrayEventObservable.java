package cn.springcloud.gray.event.server;

import java.util.List;

/**
 * @author saleson
 * @date 2020-08-16 04:13
 */
public interface GrayEventObservable {

    void addObserver(GrayEventObserver observer);

    void removeObserver(GrayEventObserver observer);

    List<GrayEventObserver> getObservers();
}
