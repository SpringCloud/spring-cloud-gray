package cn.springlcoud.gray.code.component.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author saleson
 * @date 2020-02-05 12:35
 */
@Slf4j
public abstract class SpringInitializer implements InitializingBean, ApplicationContextAware {

    protected ApplicationContext cxt;


    protected abstract void initialization();


    protected <T> T getBean(String beanName, Class<T> cls) {
        T t = null;
        try {
            t = cxt.getBean(beanName, cls);
        } catch (BeansException e) {
            log.warn("没有从spring容器中找到name为'{}', class为'{}'的Bean", beanName, cls);
        }
        if (t == null) {
            t = cxt.getBean(cls);
        }
        return t;
    }

    protected <T> T getBean(String beanName, Class<T> cls, T defaultBean) {
        try {
            return getBean(beanName, cls);
        } catch (BeansException e) {
            log.warn("没有从spring容器中找到name为'{}', class为'{}'的Bean, 返回默认的bean:{}", beanName, cls, defaultBean);
            return defaultBean;
        }
    }

    protected <T> T getBeanNullable(String beanName, Class<T> cls) {
        try {
            return getBean(beanName, cls);
        } catch (BeansException e) {
            log.warn("没有从spring容器中找到name为'{}', class为'{}'的Bean", beanName, cls);
            return null;
        }
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
