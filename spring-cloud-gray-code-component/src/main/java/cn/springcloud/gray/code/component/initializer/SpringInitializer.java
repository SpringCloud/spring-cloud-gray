package cn.springcloud.gray.code.component.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-02-05 12:35
 */
@Slf4j
public abstract class SpringInitializer implements InitializingBean, ApplicationContextAware {

    protected ApplicationContext cxt;


    protected abstract void initialization();

    protected <T> T getBean(Class<T> cls) {
        try {
            return cxt.getBean(cls);
        } catch (NoUniqueBeanDefinitionException e) {
            throw e;
        } catch (BeansException e) {
            log.warn("没有从spring容器中找到class为'{}'的Bean", cls);
        }
        return null;
    }

    protected <T> T getBean(Class<T> cls, T defaultBean) {
        T bean = getBean(cls);
        if (Objects.isNull(bean)) {
            return defaultBean;
        }
        return bean;
    }


    protected <T> T getBean(String beanName, Class<T> cls) {
        T t = null;
        try {
            t = cxt.getBean(beanName, cls);
        } catch (BeansException e) {
            log.warn("没有从spring容器中找到name为'{}', class为'{}'的Bean", beanName, cls);
        }
        if (t == null) {
            t = getBean(cls);
        }
        return t;
    }

    protected <T> T getBean(String beanName, Class<T> cls, T defaultBean) {
        T bean = getBean(beanName, cls);
        if (Objects.isNull(bean)) {
            return defaultBean;
        }
        return bean;
    }

    protected <T> T getBeanNullable(String beanName, Class<T> cls) {
        try {
            return getBean(beanName, cls);
        } catch (BeansException e) {
            log.warn("没有从spring容器中找到name为'{}', class为'{}'的Bean", beanName, cls);
            return null;
        }
    }

    protected <T> List<T> getBeans(Class<T> cls) {
        Map<String, T> beans = cxt.getBeansOfType(cls);
        return new ArrayList<>(beans.values());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialization();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.cxt = applicationContext;
    }
}
