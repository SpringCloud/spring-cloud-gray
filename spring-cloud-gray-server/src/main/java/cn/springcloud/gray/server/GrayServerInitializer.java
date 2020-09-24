package cn.springcloud.gray.server;

import cn.springcloud.gray.event.server.GrayEventObservable;
import cn.springcloud.gray.event.server.GrayEventObserver;
import cn.springcloud.gray.event.server.GrayEventTrigger;
import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.server.oauth2.Oauth2Service;
import cn.springcloud.gray.utils.SpringApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.Objects;

@Slf4j
public class GrayServerInitializer implements ApplicationContextAware, InitializingBean {
    private ApplicationContext cxt;

    @Override
    public void afterPropertiesSet() throws Exception {
        GrayServerHolder.setUserModule(SpringApplicationContextUtils.getBean("userModule", UserModule.class));
        GrayServerHolder.setOauth2Service(SpringApplicationContextUtils.getBean("oauth2Service", Oauth2Service.class));

        bindGrayEventObservers();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.cxt = applicationContext;
        SpringApplicationContextUtils.setDefaultApplicationContext(cxt);
    }


    private void bindGrayEventObservers() {
        List<GrayEventObserver> observers = SpringApplicationContextUtils.getBeans(cxt, GrayEventObserver.class);
        GrayEventTrigger grayEventTrigger = SpringApplicationContextUtils.getBean("grayEventTrigger", GrayEventTrigger.class);
        bindGrayEventObservers(grayEventTrigger, observers);
    }

    private void bindGrayEventObservers(GrayEventObservable grayEventObservable, List<GrayEventObserver> observers) {
        if (Objects.isNull(grayEventObservable)) {
            return;
        }
        for (GrayEventObserver observer : observers) {
            grayEventObservable.addObserver(observer);
        }
    }

}
