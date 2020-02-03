package cn.springlcoud.gray.event.server;

/**
 * @author saleson
 * @date 2020-02-01 16:01
 */
public interface GrayEventRetriever {


    GrayEventRetrieveResult retrieveGreaterThan(long sortMark);


    long getNewestSortMark();

}
