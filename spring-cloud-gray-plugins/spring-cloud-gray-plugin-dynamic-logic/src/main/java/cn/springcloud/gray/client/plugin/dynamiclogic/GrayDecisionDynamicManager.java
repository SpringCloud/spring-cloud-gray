package cn.springcloud.gray.client.plugin.dynamiclogic;

import cn.springcloud.gray.decision.factory.GrayDecisionFactory;
import cn.springcloud.gray.dynamiclogic.DynamicLogicDefinition;
import cn.springcloud.gray.dynamiclogic.DynamicLogicDriver;
import cn.springcloud.gray.dynamiclogic.SimpleDynamicLogicManager;

/**
 * @author saleson
 * @date 2019-12-28 10:23
 */
public class GrayDecisionDynamicManager extends SimpleDynamicLogicManager {


    public GrayDecisionDynamicManager(DynamicLogicDriver dynamicLogicDriver) {
        super(dynamicLogicDriver);
    }

    @Override
    public DynamicLogicDriver getDynamicLogicDriver() {
        return dynamicLogicDriver;
    }

    @Override
    public GrayDecisionFactory compleAndRegister(DynamicLogicDefinition dynamicLogicDefinition) {
        GrayDecisionFactory grayDecisionFactory = compleGrayDecisionFactory(dynamicLogicDefinition);
        beans.put(grayDecisionFactory.name(), grayDecisionFactory);
        return grayDecisionFactory;
    }

    @Override
    public GrayDecisionFactory compleAndRegister(String alias, DynamicLogicDefinition dynamicLogicDefinition) {
        GrayDecisionFactory grayDecisionFactory = compleGrayDecisionFactory(dynamicLogicDefinition);
        beans.put(alias, grayDecisionFactory);
        return grayDecisionFactory;
    }


    private GrayDecisionFactory compleGrayDecisionFactory(DynamicLogicDefinition dynamicLogicDefinition) {
        Object bean = getDynamicLogicDriver().compleAndInstance(dynamicLogicDefinition);
        if (!GrayDecisionFactory.class.isInstance(bean)) {
            throw new ClassCastException(bean.getClass() + "can not cast GrayDecisionFactory");
        }
        return (GrayDecisionFactory) bean;
    }

}
