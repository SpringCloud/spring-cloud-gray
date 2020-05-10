package cn.springcloud.gray.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * @author saleson
 * @date 2020-05-10 18:38
 */
@Slf4j
public class SpringApplicationContextUtils {

    private static ApplicationContext defaultApplicationContext;


    public static ApplicationContext getDefaultApplicationContext() {
        return defaultApplicationContext;
    }

    public static void setDefaultApplicationContext(ApplicationContext defaultApplicationContext) {
        SpringApplicationContextUtils.defaultApplicationContext = defaultApplicationContext;
    }


    public static <T> T getBean(String beanName, Class<T> cls) {
        return getBean(defaultApplicationContext, beanName, cls);
    }

    public static <T> T getBean(String beanName, Class<T> cls, T defaultBean) {
        return getBean(defaultApplicationContext, beanName, cls, defaultBean);
    }

    public static <T> T getBeanNullable(String beanName, Class<T> cls) {
        return getBeanNullable(defaultApplicationContext, beanName, cls);
    }


    public static <T> T getBean(ApplicationContext cxt, String beanName, Class<T> cls) {
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

    public static <T> T getBean(ApplicationContext cxt, String beanName, Class<T> cls, T defaultBean) {
        try {
            return getBean(cxt, beanName, cls);
        } catch (BeansException e) {
            log.warn("没有从spring容器中找到name为'{}', class为'{}'的Bean, 返回默认的bean:{}", beanName, cls, defaultBean);
            return defaultBean;
        }
    }

    public static <T> T getBeanNullable(ApplicationContext cxt, String beanName, Class<T> cls) {
        try {
            return getBean(cxt, beanName, cls);
        } catch (BeansException e) {
            log.warn("没有从spring容器中找到name为'{}', class为'{}'的Bean", beanName, cls);
            return null;
        }
    }


}
