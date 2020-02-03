package cn.springcloud.gray.server.module.gray.jpa;

import cn.springcloud.gray.server.module.gray.GrayEventLogModule;
import cn.springcloud.gray.server.module.gray.domain.GrayEventLog;
import cn.springcloud.gray.server.service.GrayEventLogService;

import java.util.Date;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-01-31 22:52
 */
public class JPAGrayEventLogModule implements GrayEventLogModule {

    private GrayEventLogService grayEventLogService;

    public JPAGrayEventLogModule(GrayEventLogService grayEventLogService) {
        this.grayEventLogService = grayEventLogService;
    }

    @Override
    public GrayEventLog persist(GrayEventLog grayEventLog) {
        if (Objects.isNull(grayEventLog.getCreateTime())) {
            grayEventLog.setCreateTime(new Date());
        }
        if (Objects.isNull(grayEventLog.getDelFlag())) {
            grayEventLog.setDelFlag(false);
        }
        return grayEventLogService.saveModel(grayEventLog);
    }
}
