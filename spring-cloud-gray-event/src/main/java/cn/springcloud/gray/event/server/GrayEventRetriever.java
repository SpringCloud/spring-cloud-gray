package cn.springcloud.gray.event.server;

import cn.springcloud.gray.event.GrayEvent;
import cn.springcloud.gray.event.GrayEventRetrieveResult;

/**
 * @author saleson
 * @date 2020-02-01 16:01
 */
public interface GrayEventRetriever {


    GrayEventRetrieveResult retrieveGreaterThan(long sortMark);

    Class<? extends GrayEvent> retrieveTypeClass(String type);

    long getNewestSortMark();

}
