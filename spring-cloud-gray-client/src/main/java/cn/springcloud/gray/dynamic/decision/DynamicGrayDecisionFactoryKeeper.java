package cn.springcloud.gray.dynamic.decision;

import cn.springcloud.gray.decision.DefaultGrayDecisionFactoryKeeper;
import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;
import cn.springcloud.gray.decision.factory.GrayDecisionFactory;
import cn.springcloud.gray.dynamiclogic.DynamicLogicManager;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2019-12-26 08:26
 */
public class DynamicGrayDecisionFactoryKeeper extends DefaultGrayDecisionFactoryKeeper implements GrayDecisionFactoryKeeper {

    public static final String GRAY_DECISION_DYNAMIC_TYPE = "GrayDecisionFactory";

    private DynamicLogicManager dynamicLogicManager;

    public DynamicGrayDecisionFactoryKeeper(
            DynamicLogicManager dynamicLogicManager,
            ConversionService conversionService,
            Validator validator,
            List<GrayDecisionFactory> decisionFactories) {
        super(conversionService, validator, decisionFactories);
        this.dynamicLogicManager = dynamicLogicManager;
    }


    @Override
    public GrayDecisionFactory getDecisionFactory(String name) {
        GrayDecisionFactory grayDecisionFactory = super.getDecisionFactory(name);
        if (!Objects.isNull(grayDecisionFactory)) {
            return grayDecisionFactory;
        }
        return dynamicLogicManager.getDnamicInstance(name);
    }

}
