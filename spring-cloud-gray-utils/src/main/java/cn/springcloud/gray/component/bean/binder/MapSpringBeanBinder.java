package cn.springcloud.gray.component.bean.binder;

import cn.springcloud.gray.component.spring.validator.NoOpValidator;
import cn.springcloud.gray.utils.ConfigurationUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.validation.Validator;

import java.util.Map;

/**
 * @author saleson
 * @date 2020-05-17 20:53
 */
public class MapSpringBeanBinder implements BeanBinder<Map> {

    private SpelExpressionParser parser;
    private BeanFactory beanFactory;
    private Validator validator;
    private ConversionService conversionService;


    public MapSpringBeanBinder(SpelExpressionParser parser, ConversionService conversionService) {
        this(parser, new NoOpValidator(), conversionService);
    }

    public MapSpringBeanBinder(
            SpelExpressionParser parser,
            Validator validator,
            ConversionService conversionService) {
        this(parser, null, validator, conversionService);
    }

    public MapSpringBeanBinder(
            SpelExpressionParser parser,
            BeanFactory beanFactory,
            Validator validator,
            ConversionService conversionService) {
        this.parser = parser;
        this.beanFactory = beanFactory;
        this.validator = validator;
        this.conversionService = conversionService;
    }


    @Override
    public void binding(Object obj, Map infos) {
        Map<String, Object> properties = ConfigurationUtils.normalize(infos, parser, beanFactory);
        ConfigurationUtils.bind(obj, properties,
                "", "", validator,
                conversionService);
    }
}
