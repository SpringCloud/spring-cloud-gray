package cn.springcloud.gray.server.netflix.eureka;

import cn.springcloud.gray.model.InstanceStatus;
import com.netflix.appinfo.InstanceInfo;

public class EurekaInstatnceTransformer {


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
