package cn.springcloud.gray.server.manager;


import cn.springcloud.gray.server.module.GrayModule;

/**
 * 灰度服务管理类，属于服务端的类。主要是编辑服务实例，编辑灰度策略，以及维护最新的灰度列表。
 */
public interface GrayServiceManager {

    GrayModule getGrayModule();


    /**
     * 打开检查
     */
    void openForWork();


    void shutdown();
}
