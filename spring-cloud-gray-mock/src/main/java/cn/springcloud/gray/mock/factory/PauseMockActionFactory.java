package cn.springcloud.gray.mock.factory;

import cn.springcloud.gray.component.bean.binder.BeanBinder;
import cn.springcloud.gray.component.bean.binder.MapSpringBeanBinder;
import cn.springcloud.gray.mock.MockAction;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Map;

/**
 * 暂停/休眠操作
 *
 * @author saleson
 * @date 2020-05-17 23:09
 */
@Slf4j
public class PauseMockActionFactory extends AbstractMockActionFactory<PauseMockActionFactory.Config, Map> {


    public PauseMockActionFactory() {
        this(new MapSpringBeanBinder(new SpelExpressionParser(), DefaultConversionService.getSharedInstance()));
    }


    public PauseMockActionFactory(BeanBinder<Map> beanBinder) {
        super(beanBinder);
    }

    @Override
    public <RESULT> MockAction<RESULT> apply(Config config) {
        return args -> {
            if (log.isDebugEnabled()) {
                log.debug("pause/sleep {} ms", config.getTime());
            }
            if (config.getTime() < 1) {
                return null;
            }
            try {
                Thread.sleep(config.getTime());
            } catch (InterruptedException e) {

            }
            return null;

        };
    }

    @Data
    public static class Config {
        private long time;
    }
}
