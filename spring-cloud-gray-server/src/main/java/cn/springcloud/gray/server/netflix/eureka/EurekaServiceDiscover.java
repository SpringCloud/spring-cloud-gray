package cn.springcloud.gray.server.netflix.eureka;

import cn.springcloud.gray.server.discovery.InstanceInfo;
import cn.springcloud.gray.server.discovery.ServiceDiscover;
import cn.springcloud.gray.server.discovery.ServiceInfo;
import cn.springcloud.gray.server.module.domain.InstanceStatus;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EurekaServiceDiscover implements ServiceDiscover {

    private EurekaClient eurekaClient;

    public EurekaServiceDiscover(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    @Override
    public List<ServiceInfo> listAllSerivceInfos() {
        Applications applications = eurekaClient.getApplications();
        if (applications == null) {
            return Collections.emptyList();
        }
        List<Application> applicationList = applications.getRegisteredApplications();
        return applicationList.stream().map(this::ofApplication).collect(Collectors.toList());
    }

    @Override
    public ServiceInfo getServiceInfo(String serviceId) {
        Application application = eurekaClient.getApplication(serviceId);
        if (application != null) {
            return ofApplication(application);
        }
        return null;
    }


    @Override
    public List<InstanceInfo> listInstanceInfos(String serviceId) {
        Application application = eurekaClient.getApplication(serviceId);
        if (application != null) {
            return Collections.emptyList();
        }

        List<com.netflix.appinfo.InstanceInfo> eurekaInstanceInfos = application.getInstances();
        return eurekaInstanceInfos.stream().map(this::ofInstance).collect(Collectors.toList());
    }

    @Override
    public InstanceInfo getInstanceInfo(String serviceId, String instanceId) {

        return null;
    }

    private ServiceInfo ofApplication(Application application) {
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setServiceId(application.getName());
        return serviceInfo;
    }

    private InstanceInfo ofInstance(com.netflix.appinfo.InstanceInfo eurekaInstanceInfo) {
        InstanceInfo instanceInfo = new InstanceInfo();
        instanceInfo.setServiceId(eurekaInstanceInfo.getAppName());
        instanceInfo.setInstanceId(eurekaInstanceInfo.getInstanceId());
        instanceInfo.setInstanceStatus(ofEurekaInstanceStatus(eurekaInstanceInfo.getStatus()));
        return instanceInfo;
    }

    private InstanceStatus ofEurekaInstanceStatus(com.netflix.appinfo.InstanceInfo.InstanceStatus eurekaInstanceStatus) {
        if (eurekaInstanceStatus == null) {
            return InstanceStatus.UNKNOWN;
        }
        switch (eurekaInstanceStatus) {
            case DOWN:
                return InstanceStatus.DOWN;
            case UP:
                return InstanceStatus.UP;
            default:
                return InstanceStatus.UNKNOWN;
        }
    }
}
