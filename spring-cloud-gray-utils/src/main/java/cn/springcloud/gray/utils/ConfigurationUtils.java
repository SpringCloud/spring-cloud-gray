package cn.springcloud.gray.utils;

import cn.springcloud.gray.bean.properties.bind.Bindable;
import cn.springcloud.gray.bean.properties.bind.Binder;
import cn.springcloud.gray.bean.properties.source.MapConfigurationPropertySource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationUtils {


    public static Map<String, Object> normalize(Map<String, String> args,
                                                SpelExpressionParser parser,
                                                BeanFactory beanFactory) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, String> entry : args.entrySet()) {
            String key = entry.getKey();
            Object value = getValue(parser, beanFactory, entry.getValue());

            map.put(key, value);
        }
        return map;
    }


    public static void bind(Object o, Map<String, Object> properties,
                            String configurationPropertyName, String bindingName, Validator validator,
                            ConversionService conversionService) {
        Object toBind = getTargetObject(o);

        new Binder(
                Collections.singletonList(new MapConfigurationPropertySource(properties)),
                null, conversionService).bind(configurationPropertyName,
                Bindable.ofInstance(toBind));

        if (validator != null) {
            BindingResult errors = new BeanPropertyBindingResult(toBind, bindingName);
            validator.validate(toBind, errors);
            if (errors.hasErrors()) {
                throw new RuntimeException(new BindException(errors));
            }
        }
    }


    private static <T> T getTargetObject(Object candidate) {
        try {
            if (AopUtils.isAopProxy(candidate) && (candidate instanceof Advised)) {
                return (T) ((Advised) candidate).getTargetSource().getTarget();
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to unwrap proxied object", ex);
        }
        return (T) candidate;
    }


    private static Object getValue(SpelExpressionParser parser, BeanFactory beanFactory,
                                   String entryValue) {
        Object value;
        String rawValue = entryValue;
        if (rawValue != null) {
            rawValue = rawValue.trim();
        }
        if (rawValue != null && rawValue.startsWith("#{") && entryValue.endsWith("}")) {
            // assume it's spel
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setBeanResolver(new BeanFactoryResolver(beanFactory));
            Expression expression = parser.parseExpression(entryValue,
                    new TemplateParserContext());
            context.lookupVariable("api");
            value = expression.getValue(context);
        } else {
            value = entryValue;
        }
        return value;
    }
}
