package cn.springcloud.gray.decision;

import cn.springcloud.gray.decision.factory.GrayDecisionFactory;
import cn.springcloud.gray.model.DecisionDefinition;
import cn.springcloud.gray.utils.ConfigurationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DefaultGrayDecisionFactoryKeeper implements GrayDecisionFactoryKeeper, ApplicationContextAware {

    private ApplicationContext cxt;
    private SpelExpressionParser parser = new SpelExpressionParser();
    private ConversionService conversionService;
    private Validator validator;

    private Map<String, GrayDecisionFactory> grayDecisionFactories = new HashMap<>();


    public DefaultGrayDecisionFactoryKeeper(ConversionService conversionService, Validator validator, List<GrayDecisionFactory> decisionFactories) {
        this.conversionService = conversionService;
        this.validator = validator;
        initGrayDecisionFactories(decisionFactories);
    }


    @Override
    public GrayDecisionFactory getDecisionFactory(String name) {
        return grayDecisionFactories.get(name);
    }

    @Override
    public GrayDecision getGrayDecision(DecisionDefinition decisionDefinition) {
        GrayDecisionFactory factory = getDecisionFactory(decisionDefinition.getName());
        if (factory == null) {
            log.error("没有找到灰度决定工厂:{}", decisionDefinition.getName());
            throw new NullPointerException("没有找到灰度决定工厂:" + decisionDefinition.getName());
        }
        return factory.apply(configuration -> {
            Map<String, Object> properties = ConfigurationUtils.normalize(decisionDefinition.getInfos(), parser, cxt);
            ConfigurationUtils.bind(configuration, properties,
                    "", "", validator,
                    conversionService);
        });
    }


    private void initGrayDecisionFactories(List<GrayDecisionFactory> decisionFactories) {
        decisionFactories.stream().forEach(factory -> {
            grayDecisionFactories.put(factory.name(), factory);
        });

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.cxt = applicationContext;
    }
}
