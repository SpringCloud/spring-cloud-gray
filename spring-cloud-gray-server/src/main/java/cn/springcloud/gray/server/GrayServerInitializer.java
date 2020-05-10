package cn.springcloud.gray.server;

import cn.springcloud.gray.server.module.user.UserModule;
import cn.springcloud.gray.utils.SpringApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Slf4j
public class GrayServerInitializer implements ApplicationContextAware, InitializingBean {
    private ApplicationContext cxt;

    @Override
    public void afterPropertiesSet() throws Exception {
        GrayServerHolder.setUserModule(SpringApplicationContextUtils.getBean("userModule", UserModule.class));

    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.cxt = applicationContext;
        SpringApplicationContextUtils.setDefaultApplicationContext(cxt);
    }


}
