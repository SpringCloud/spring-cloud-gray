package cn.springcloud.gray.client.plugin.dynamiclogic;

import cn.springcloud.gray.dynamiclogic.DynamicLogicDefinition;
import cn.springcloud.gray.dynamiclogic.DynamicLogicDriver;
import cn.springcloud.gray.dynamiclogic.DynamicLogicManager;
import com.fm.compiler.dynamicbean.CompliteDefinition;
import com.fm.compiler.dynamicbean.DynamicBeanManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author saleson
 * @date 2019-12-28 10:19
 */
public class FmDynamicLogicDriver implements DynamicLogicDriver {

    private Map<String, DynamicLogicManager> dynamicLogicManagers = new ConcurrentHashMap<>();
    private DynamicBeanManager dynamicBeanManager;

    public FmDynamicLogicDriver(DynamicBeanManager dynamicBeanManager) {
        this.dynamicBeanManager = dynamicBeanManager;
    }

    @Override
    public <T> T compleAndInstance(DynamicLogicDefinition dynamicLogicDefinition) {
        CompliteDefinition compliteDefinition = CompliteDefinition.builder()
                .code(dynamicLogicDefinition.getCode())
                .name(dynamicLogicDefinition.getName())
                .language(dynamicLogicDefinition.getLanguage())
                .build();
        return dynamicBeanManager.compleAndInstance(compliteDefinition);
    }

    @Override
    public void loadDynamicLogicManager(String type, DynamicLogicManager dynamicLogicManager) {
        dynamicLogicManagers.put(type, dynamicLogicManager);
    }

    @Override
    public DynamicLogicManager getDynamicLogicManager(String type) {
        return dynamicLogicManagers.get(type);
    }


}
