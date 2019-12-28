package cn.springcloud.gray.dynamiclogic;

import java.util.Objects;

/**
 * @author saleson
 * @date 2019-12-26 08:00
 */
public interface DynamicLogicDriver {


    default <T> T compleAndRegister(String type, String alias, DynamicLogicDefinition dynamicLogicDefinition) {
        DynamicLogicManager dynamicLogicManager = getDynamicLogicManager(type);
        if (Objects.isNull(dynamicLogicManager)) {
            throw new NullPointerException("dynamicLogicManager is null");
        }
        return dynamicLogicManager.compleAndRegister(alias, dynamicLogicDefinition);
    }

    <T> T compleAndInstance(DynamicLogicDefinition dynamicLogicDefinition);

    void loadDynamicLogicManager(String type, DynamicLogicManager dynamicLogicManager);

    DynamicLogicManager getDynamicLogicManager(String type);

}
