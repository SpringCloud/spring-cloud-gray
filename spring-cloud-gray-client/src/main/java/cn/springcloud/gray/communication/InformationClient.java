package cn.springcloud.gray.communication;

import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayTrackDefinition;

import java.util.List;


/**
 * 该接口主要是负责和灰度服务端进行通信，获取灰度列表，编辑灰度实例等能力。其实现类HttpInformationClient默认使用http方式访问灰度服务端。
 * 子类InformationClientDecorator是一个适配器类，RetryableInformationClient继承了InformationClientDecorator类，实现了重试的功能。
 */
public interface InformationClient {


    /**
     * 返回在灰度中注册的所有灰度实例(不包括非灰度实例)
     *
     * @return 所有的灰度实例
     */
    List<GrayInstance> allGrayInstances();

    /**
     * 注册灰度实例
     *
     * @param grayInstance 服务实例id
     */
    void addGrayInstance(GrayInstance grayInstance);

    /**
     * 获取灰度实例的信息
     *
     * @param serviceId  服务id
     * @param instanceId 实例id
     * @return gray instance info
     */
    GrayInstance getGrayInstance(String serviceId, String instanceId);


    /**
     * 灰度实例下线
     *
     * @param instanceId 实例id
     */
    void serviceDownline(String instanceId);

    List<GrayTrackDefinition> getTrackDefinitions(String serviceId, String instanceId);
}
