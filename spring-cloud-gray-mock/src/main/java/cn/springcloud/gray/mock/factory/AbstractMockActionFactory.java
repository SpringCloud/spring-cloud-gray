package cn.springcloud.gray.mock.factory;

import cn.springcloud.gray.component.bean.binder.BeanBinder;
import cn.springcloud.gray.mock.MockAction;
import cn.springcloud.gray.utils.GenericUtils;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-17 22:34
 */
public abstract class AbstractMockActionFactory<CONFIG, DEFINITION> implements MockActionFactory<CONFIG, DEFINITION> {

    private BeanBinder<DEFINITION> beanBinder;


    public AbstractMockActionFactory(BeanBinder<DEFINITION> beanBinder) {
        this.beanBinder = beanBinder;
    }

    @Override
    public <RESULT> MockAction<RESULT> create(DEFINITION definition) {
        CONFIG config = newConfig();
        if (Objects.nonNull(config)) {
            beanBinder.binding(config, definition);
        }
        return apply(config);
    }


    @Override
    public CONFIG newConfig() {
        Class<?> cls = GenericUtils.getGenericClass(getClass(), MockActionFactory.class, 0);
        if (Objects.isNull(cls)) {
            return null;
        }
        return (CONFIG) BeanUtils.instantiateClass(cls);
    }

}
