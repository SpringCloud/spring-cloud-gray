package cn.springlcoud.gray.event.server;

import cn.springlcoud.gray.event.GrayEvent;
import cn.springlcoud.gray.event.GrayEventRetrieveResult;

/**
 * @author saleson
 * @date 2020-02-01 16:01
 */
public interface GrayEventRetriever {


    GrayEventRetrieveResult retrieveGreaterThan(long sortMark);

    Class<? extends GrayEvent> retrieveTypeClass(String type);

    long getNewestSortMark();

}
