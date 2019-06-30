package cn.springcloud.gray;

import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerListProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

@Slf4j
public class GrayClientInitializer implements ApplicationContextAware, InitializingBean {
    private ApplicationContext cxt;

    @Override
    public void afterPropertiesSet() throws Exception {
        GrayClientHolder.setGrayManager(getBean("grayManager", GrayManager.class));
        GrayClientHolder.setRequestLocalStorage(getBean("requestLocalStorage", RequestLocalStorage.class));
        GrayClientHolder.setServerExplainer(getBean("serverExplainer", ServerExplainer.class));
        GrayClientHolder.setServerListProcessor(
                getBean("serverListProcessor", ServerListProcessor.class, new ServerListProcessor.Default()));

        initGrayManagerRequestInterceptors();

    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.cxt = applicationContext;
    }


    private <T> T getBean(String beanName, Class<T> cls) {
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

    private <T> T getBean(String beanName, Class<T> cls, T defaultBean) {
        try {
            return getBean(beanName, cls);
        } catch (BeansException e) {
            log.warn("没有从spring容器中找到name为'{}', class为'{}'的Bean, 返回默认的bean:{}", beanName, cls, defaultBean);
            return defaultBean;
        }
    }

    private <T> T getBeanNullable(String beanName, Class<T> cls) {
        try {
            return getBean(beanName, cls);
        } catch (BeansException e) {
            log.warn("没有从spring容器中找到name为'{}', class为'{}'的Bean", beanName, cls);
            return null;
        }
    }


    /**
     * 为了解耦合，特别将GrayManger加载Sping容器中的RequestInterceptor的逻辑独立出来
     */
    private void initGrayManagerRequestInterceptors() {
        Map<String, RequestInterceptor> requestInterceptors = cxt.getBeansOfType(RequestInterceptor.class);
        GrayManager grayManager = GrayClientHolder.getGrayManager();
        if (grayManager instanceof UpdateableGrayManager) {
            ((UpdateableGrayManager) grayManager).setRequestInterceptors(requestInterceptors.values());
        }
        grayManager.setup();
    }
}
