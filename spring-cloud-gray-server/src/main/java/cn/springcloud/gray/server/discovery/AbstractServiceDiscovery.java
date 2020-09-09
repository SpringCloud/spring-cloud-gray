package cn.springcloud.gray.server.discovery;

import cn.springcloud.gray.model.InstanceInfo;

import java.util.Collections;
import java.util.List;

/**
 * @author saleson
 * @date 2020-09-09 08:25
 */
public abstract class AbstractServiceDiscovery<SI> implements ServiceDiscovery {

    private List<InstanceInfoAnalyser<SI>> instanceInfoAnalysers = Collections.emptyList();

    protected InstanceInfo convertToInstanceInfo(SI instance) {
        InstanceInfo instanceInfo = createInstanceInfo(instance);
        analyseInstanceInfo(instance, instanceInfo);
        return instanceInfo;
    }

    protected abstract InstanceInfo createInstanceInfo(SI instance);


    protected void setInstanceInfoAnalysers(List<InstanceInfoAnalyser<SI>> instanceInfoAnalysers) {
        this.instanceInfoAnalysers = instanceInfoAnalysers;
    }


    protected void analyseInstanceInfo(SI instance, InstanceInfo instanceInfo) {
        instanceInfoAnalysers.forEach(analyser -> analyser.analyse(instance, instanceInfo));
    }

}
