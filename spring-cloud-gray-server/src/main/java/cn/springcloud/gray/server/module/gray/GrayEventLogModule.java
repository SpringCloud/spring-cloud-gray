package cn.springcloud.gray.server.module.gray;

import cn.springcloud.gray.server.module.gray.domain.GrayEventLog;

import java.util.List;

/**
 * @author saleson
 * @date 2020-01-31 22:05
 */
public interface GrayEventLogModule {
    GrayEventLog persist(GrayEventLog grayEventLog);

    List<GrayEventLog> queryAllGreaterThanSortMark(long sortMark);

    long getNewestSortMark();
}
