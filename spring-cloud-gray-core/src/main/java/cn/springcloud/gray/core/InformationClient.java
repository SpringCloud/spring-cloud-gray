package cn.springcloud.gray.core;

import java.util.List;


/**
 * 该接口主要是负责和灰度服务端进行通信，获取灰度列表，编辑灰度实例等能力。其实现类HttpInformationClient默认使用http方式访问灰度服务端。
 * 子类InformationClientDecorator是一个适配器类，RetryableInformationClient继承了InformationClientDecorator类，实现了重试的功能。
 */
public interface InformationClient {


    /**
     * 返回在灰度中注册的所有实例(包括非灰度实例)
     *
     * @return
     */
    List<GrayService> listGrayService();

    /**
     * 根据serviceId返回灰度服务对象
     *
     * @param serviceId
     * @return
     */
    GrayService grayService(String serviceId);


    /**
     * 返回注册的实例对象
     *
     * @param serviceId
     * @param instanceId
     * @return
     */
    GrayInstance grayInstance(String serviceId, String instanceId);


    /**
     * 注册灰度实例
     *
     * @param serviceId
     * @param instanceId
     */
    void addGrayInstance(String serviceId, String instanceId);


    /**
     * 灰度实例下线
     */
    void serviceDownline();


    /**
     * 灰度实例下线
     *
     * @param serviceId
     * @param instanceId
     */
    void serviceDownline(String serviceId, String instanceId);

}
