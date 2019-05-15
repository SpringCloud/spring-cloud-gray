package cn.springcloud.gray;

import cn.springcloud.gray.communication.InformationClient;

public interface CommunicableGrayManager extends GrayManager {

    InformationClient getGrayInformationClient();

}
