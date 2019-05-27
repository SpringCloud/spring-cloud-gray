package cn.springcloud.gray.client.netflix.eureka;

import cn.springcloud.gray.model.InstanceStatus;
import com.netflix.appinfo.InstanceInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EurekaInstatnceTransformer {


    public static InstanceInfo.InstanceStatus toEurekaInstanceStatus(InstanceStatus status) {
        switch (status) {
            case UP:
                return InstanceInfo.InstanceStatus.UP;
            case DOWN:
                return InstanceInfo.InstanceStatus.DOWN;
            case UNKNOWN:
                return InstanceInfo.InstanceStatus.UNKNOWN;
            case STARTING:
                return InstanceInfo.InstanceStatus.STARTING;
            case OUT_OF_SERVICE:
                return InstanceInfo.InstanceStatus.OUT_OF_SERVICE;
            default:
                log.error("不支持{}类型的实例状态", status);
                throw new UnsupportedOperationException("不支持的实例状态");

        }
    }


    public static InstanceStatus toGrayInstanceStatus(InstanceInfo.InstanceStatus status) {
        if (status == null) {
            return InstanceStatus.UNKNOWN;
        }
        switch (status) {
            case DOWN:
                return InstanceStatus.DOWN;
            case UP:
                return InstanceStatus.UP;
            case STARTING:
                return InstanceStatus.STARTING;
            case OUT_OF_SERVICE:
                return InstanceStatus.OUT_OF_SERVICE;
            default:
                return InstanceStatus.UNKNOWN;
        }
    }


}
