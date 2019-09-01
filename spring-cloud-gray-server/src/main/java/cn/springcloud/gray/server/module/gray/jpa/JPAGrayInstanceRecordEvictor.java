package cn.springcloud.gray.server.module.gray.jpa;

import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.server.module.gray.GrayInstanceRecordEvictor;
import cn.springcloud.gray.server.module.gray.domain.GrayInstance;
import cn.springcloud.gray.server.service.GrayInstanceService;

import java.util.List;
import java.util.Set;

public class JPAGrayInstanceRecordEvictor implements GrayInstanceRecordEvictor {

    private GrayInstanceService grayInstanceService;
    /**
     * 将被清理的状态
     */
    private Set<InstanceStatus> evictionInstanceStatus;


    /**
     * 最后更新时间过期天数
     */
    private int lastUpdateDateExpireDays;


    public JPAGrayInstanceRecordEvictor(
            GrayInstanceService grayInstanceService,
            Set<InstanceStatus> evictionInstanceStatus,
            int lastUpdateDateExpireDays) {
        this.grayInstanceService = grayInstanceService;
        this.evictionInstanceStatus = evictionInstanceStatus;
        this.lastUpdateDateExpireDays = lastUpdateDateExpireDays;
    }

    @Override
    public void evict() {
        List<GrayInstance> grayInstances =
                grayInstanceService.findAllByEvictableRecords(lastUpdateDateExpireDays, evictionInstanceStatus);
        grayInstances.forEach(grayInstance -> grayInstanceService.deleteReactById(grayInstance.getInstanceId()));
    }
}
