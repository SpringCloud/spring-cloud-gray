package cn.springcloud.gray.server.netflix.eureka;

import cn.springcloud.gray.model.InstanceInfo;
import cn.springcloud.gray.model.InstanceStatus;
import cn.springcloud.gray.server.discovery.AbstractServiceDiscovery;
import cn.springcloud.gray.server.discovery.InstanceInfoAnalyser;
import cn.springcloud.gray.server.discovery.ServiceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EurekaServiceDiscovery extends AbstractServiceDiscovery<com.netflix.appinfo.InstanceInfo> {

    private EurekaClient eurekaClient;

    public EurekaServiceDiscovery(EurekaClient eurekaClient) {
        this(eurekaClient, Collections.EMPTY_LIST);
    }

    public EurekaServiceDiscovery(
            EurekaClient eurekaClient,
            List<InstanceInfoAnalyser<com.netflix.appinfo.InstanceInfo>> instanceInfoAnalysers) {
        this.eurekaClient = eurekaClient;
        this.setInstanceInfoAnalysers(instanceInfoAnalysers);
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
        if (application == null) {
            return Collections.emptyList();
        }

        List<com.netflix.appinfo.InstanceInfo> eurekaInstanceInfos = application.getInstancesAsIsFromEureka();
        return eurekaInstanceInfos.stream().map(this::ofInstance).collect(Collectors.toList());
    }

    @Override
    public InstanceInfo getInstanceInfo(String serviceId, String instanceId) {
        Application application = eurekaClient.getApplication(serviceId);
        if (application != null) {
            com.netflix.appinfo.InstanceInfo eurekaInstanceInfo = application.getByInstanceId(instanceId);
            if (eurekaInstanceInfo != null) {
                return ofInstance(eurekaInstanceInfo);
            }
        }
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
        instanceInfo.setHost(eurekaInstanceInfo.getIPAddr());
        instanceInfo.setPort(eurekaInstanceInfo.getPort());
        instanceInfo.setInstanceStatus(ofEurekaInstanceStatus(eurekaInstanceInfo.getStatus()));
        return instanceInfo;
    }

    private InstanceStatus ofEurekaInstanceStatus(com.netflix.appinfo.InstanceInfo.InstanceStatus eurekaInstanceStatus) {
        return EurekaInstatnceTransformer.toGrayInstanceStatus(eurekaInstanceStatus);
    }


    @Override
    protected InstanceInfo createInstanceInfo(com.netflix.appinfo.InstanceInfo instance) {
        return ofInstance(instance);
    }
}
