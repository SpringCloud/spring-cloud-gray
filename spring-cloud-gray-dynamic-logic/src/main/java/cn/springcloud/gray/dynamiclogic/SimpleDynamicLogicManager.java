package cn.springcloud.gray.dynamiclogic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author saleson
 * @date 2019-12-28 10:23
 */
public class SimpleDynamicLogicManager implements DynamicLogicManager {

    protected Map<String, Object> beans = new ConcurrentHashMap<>();
    protected DynamicLogicDriver dynamicLogicDriver;


    public SimpleDynamicLogicManager(DynamicLogicDriver dynamicLogicDriver) {
        this.dynamicLogicDriver = dynamicLogicDriver;
    }

    @Override
    public DynamicLogicDriver getDynamicLogicDriver() {
        return dynamicLogicDriver;
    }

    @Override
    public <T> T compleAndRegister(DynamicLogicDefinition dynamicLogicDefinition) {
        return compleAndRegister(dynamicLogicDefinition.getName(), dynamicLogicDefinition);
    }

    @Override
    public <T> T compleAndRegister(String alias, DynamicLogicDefinition dynamicLogicDefinition) {
        T bean = getDynamicLogicDriver().compleAndInstance(dynamicLogicDefinition);
        beans.put(alias, bean);
        return bean;
    }

    @Override
    public <T> T getDnamicInstance(String alias) {
        return (T) beans.get(alias);
    }

    @Override
    public <T> T remove(String alias) {
        return (T) beans.remove(alias);
    }
}
