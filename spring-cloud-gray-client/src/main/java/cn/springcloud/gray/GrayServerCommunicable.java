package cn.springcloud.gray;

import cn.springcloud.gray.communication.InformationClient;

public interface GrayServerCommunicable {

    InformationClient getGrayInformationClient();
}
