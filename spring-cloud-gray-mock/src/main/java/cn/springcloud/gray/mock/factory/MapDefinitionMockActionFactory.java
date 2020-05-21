package cn.springcloud.gray.mock.factory;

import cn.springcloud.gray.component.bean.binder.BeanBinder;
import cn.springcloud.gray.component.bean.binder.MapSpringBeanBinder;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Map;

/**
 * @author saleson
 * @date 2020-05-21 00:18
 */
public abstract class MapDefinitionMockActionFactory<C> extends AbstractMockActionFactory<C, Map> {

    public MapDefinitionMockActionFactory() {
        this(new MapSpringBeanBinder(new SpelExpressionParser(), DefaultConversionService.getSharedInstance()));
    }

    public MapDefinitionMockActionFactory(BeanBinder<Map> beanBinder) {
        super(beanBinder);
    }
}
