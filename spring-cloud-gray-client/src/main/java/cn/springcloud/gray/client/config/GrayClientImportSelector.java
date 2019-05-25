package cn.springcloud.gray.client.config;


import cn.springcloud.gray.client.EnableGrayClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.commons.util.SpringFactoryImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Properties;

@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class GrayClientImportSelector extends SpringFactoryImportSelector<EnableGrayClient> {


    @Override
    public String[] selectImports(AnnotationMetadata metadata) {
        String[] imports = super.selectImports(metadata);

        Environment env = getEnvironment();
        String grayEnabled = env.getProperty("gray.enabled");
        if (StringUtils.isEmpty(grayEnabled)) {
            if (ConfigurableEnvironment.class.isInstance(env)) {
                ConfigurableEnvironment environment = (ConfigurableEnvironment) env;
                MutablePropertySources m = environment.getPropertySources();
                Properties p = new Properties();
                p.put("gray.enabled", "true");
                m.addLast(new PropertiesPropertySource("defaultProperties", p));
            }
        }

        return imports;
    }


    @Override
    protected boolean isEnabled() {
        return false;
    }

    @Override
    protected boolean hasDefaultFactory() {
        return true;
    }
}
