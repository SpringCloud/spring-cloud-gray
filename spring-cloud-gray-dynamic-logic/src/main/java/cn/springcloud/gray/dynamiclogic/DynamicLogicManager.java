package cn.springcloud.gray.dynamiclogic;

/**
 * @author saleson
 * @date 2019-12-26 08:13
 */
public interface DynamicLogicManager {

    DynamicLogicDriver getDynamicLogicDriver();

    <T> T compleAndRegister(DynamicLogicDefinition dynamicLogicDefinition);

    <T> T compleAndRegister(String alias, DynamicLogicDefinition dynamicLogicDefinition);

    <T> T getDnamicInstance(String alias);

    <T> T remove(String alias);

}
