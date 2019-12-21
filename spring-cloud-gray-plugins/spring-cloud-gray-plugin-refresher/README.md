# 通过配置中心刷新灰度实例、灰度追踪的插件

## 实现方式
监听EnvironmentChangeEvent,对比更新的keys，有涉及到灰度实例、灰度追踪的的配置更改，将会执行相应的更新操作。

## nacos示例
[点击查看nacos配置中心示例](../../spring-cloud-gray-samples/spring-cloud-gray-consumer-nacos-config-sample)

## apollo示例
[点击查看apollo配置中心示例](../../spring-cloud-gray-samples/spring-cloud-gray-apollo-config-sample)

### 扩展apollo支持
apollo原生的组件并不支持@ConfigrationProperties的更新，也不支持push EnvironmentChangeEvent，如果要支持上述两种能力，还需添加代码进行扩展。

扩展的代码挺简单，新建一个类实现apollo的com.ctrip.framework.apollo.ConfigChangeListener接口，在该接口中调用org.springframework.cloud.context.refresh.ContextRefresher#refresh()方法。例如：
```java 
import org.springframework.stereotype.Component;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import org.springframework.cloud.context.refresh.ContextRefresher;
import import javax.annotation.PostConstruct;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.google.common.base.Splitter;

@Component
public class ApolloAutoRefreshListener implements ConfigChangeListener {
    @Autowired
    private ContextRefresher contextRefresher;
    @Value("${apollo.bootstrap.namespaces:application}")
    private String namespaces;
    
    @Override
    public void onChange(ConfigChangeEvent changeEvent) {
        contextRefresher.refresh();
    }
    
    @PostConstruct
    public void init(){
        Splitter.on(",").split(namespaces).forEach(namespace -> {
            Config config = ConfigService.getConfig(namespace);
            config.addChangeListener(this);
        });
    }
}
```

也可参考[spring-cloud-gray-apollo-config-sample](../../spring-cloud-gray-samples/spring-cloud-gray-apollo-config-sample)示例项目的实现，如类ApolloAutoRefreshListener, ApolloAutoRefreshConfiguration, EnvironmentRefresher 