package cn.springcloud.gray.server.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@ConditionalOnBean(EurekaClient.class)
public class EurekaGrayService {

    private EurekaClient eurekaClient;
    private DiscoveryClient discoveryClient;
//    private final GrayServiceManager grayServiceManager;

//    @Autowired
//    public EurekaGrayService(EurekaClient eurekaClient, DiscoveryClient discoveryClient, GrayServiceManager
//            grayServiceManager) {
//        super(grayServiceManager, discoveryClient);
//        this.eurekaClient = eurekaClient;
//        this.discoveryClient = discoveryClient;
//        this.grayServiceManager = grayServiceManager;
//    }
//
//    /**
//     * 返回服务实例列表
//     *
//     * @param serviceId 服务id
//     * @return 灰度服务实例VO列表
//     */
//    @Override
//    public ResponseEntity<List<GrayInstanceVO>> instances(String serviceId) {
//        List<GrayInstanceVO> list = new ArrayList<>();
//        Application app = eurekaClient.getApplication(serviceId);
//        List<InstanceInfo> instanceInfos = app.getInstances();
//        for (InstanceInfo instanceInfo : instanceInfos) {
//            GrayInstanceVO vo = new GrayInstanceVO();
//            vo.setAppName(app.getName());
//            vo.setServiceId(serviceId);
//            vo.setInstanceId(instanceInfo.getInstanceId());
//            vo.setMetadata(instanceInfo.getMetadata());
//            vo.setUrl(instanceInfo.getHomePageUrl());
//            GrayInstance grayInstance = grayServiceManager.getGrayInstane(serviceId, instanceInfo.getInstanceId());
//            if (grayInstance != null) {
//                vo.setOpenGray(grayInstance.isOpenGray());
//                vo.setHasGrayPolicies(grayInstance.hasGrayPolicy());
//            }
//            list.add(vo);
//        }
//        return ResponseEntity.ok(list);
//    }


}
