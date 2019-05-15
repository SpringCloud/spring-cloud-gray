/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.springcloud.gray.bean.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.PropertyEditorRegistry;
import cn.springcloud.gray.bean.properties.bind.BindHandler;
import cn.springcloud.gray.bean.properties.bind.Bindable;
import cn.springcloud.gray.bean.properties.bind.Binder;
import cn.springcloud.gray.bean.properties.bind.PropertySourcesPlaceholdersResolver;
import cn.springcloud.gray.bean.properties.bind.handler.IgnoreErrorsBindHandler;
import cn.springcloud.gray.bean.properties.bind.handler.IgnoreTopLevelConverterNotFoundBindHandler;
import cn.springcloud.gray.bean.properties.bind.handler.NoUnboundElementsBindHandler;
import cn.springcloud.gray.bean.properties.bind.validation.ValidationBindHandler;
import cn.springcloud.gray.bean.properties.source.ConfigurationPropertySource;
import cn.springcloud.gray.bean.properties.source.ConfigurationPropertySources;
import cn.springcloud.gray.bean.properties.source.UnboundElementsSourceFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.PropertySources;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

/**
 * Internal class by the {@link ConfigurationPropertiesBindingPostProcessor} to handle the
 * actual {@link ConfigurationProperties} binding.
 *
 * @author Stephane Nicoll
 * @author Phillip Webb
 */
class ConfigurationPropertiesBinder {

    private final ApplicationContext applicationContext;

    private final PropertySources propertySources;

    private final Validator configurationPropertiesValidator;

    private final boolean jsr303Present;

    private volatile Validator jsr303Validator;

    private volatile Binder binder;

    ConfigurationPropertiesBinder(ApplicationContext applicationContext,
                                  String validatorBeanName) {
        this.applicationContext = applicationContext;
        this.propertySources = new PropertySourcesDeducer(applicationContext)
                .getPropertySources();
        this.configurationPropertiesValidator = getConfigurationPropertiesValidator(
                applicationContext, validatorBeanName);
        this.jsr303Present = ConfigurationPropertiesJsr303Validator
                .isJsr303Present(applicationContext);
    }

    public void bind(Bindable<?> target) {
        ConfigurationProperties annotation = target
                .getAnnotation(ConfigurationProperties.class);
        Assert.state(annotation != null,
                "Missing @ConfigurationProperties on " + target);
        List<Validator> validators = getValidators(target);
        BindHandler bindHandler = getBindHandler(annotation, validators);
        getBinder().bind(annotation.prefix(), target, bindHandler);
    }

    private Validator getConfigurationPropertiesValidator(
            ApplicationContext applicationContext, String validatorBeanName) {
        if (applicationContext.containsBean(validatorBeanName)) {
            return applicationContext.getBean(validatorBeanName, Validator.class);
        }
        return null;
    }

    private List<Validator> getValidators(Bindable<?> target) {
        List<Validator> validators = new ArrayList<>(3);
        if (this.configurationPropertiesValidator != null) {
            validators.add(this.configurationPropertiesValidator);
        }
        if (this.jsr303Present && target.getAnnotation(Validated.class) != null) {
            validators.add(getJsr303Validator());
        }
        if (target.getValue() != null && target.getValue().get() instanceof Validator) {
            validators.add((Validator) target.getValue().get());
        }
        return validators;
    }

    private Validator getJsr303Validator() {
        if (this.jsr303Validator == null) {
            this.jsr303Validator = new ConfigurationPropertiesJsr303Validator(
                    this.applicationContext);
        }
        return this.jsr303Validator;
    }

    private BindHandler getBindHandler(ConfigurationProperties annotation,
                                       List<Validator> validators) {
        BindHandler handler = new IgnoreTopLevelConverterNotFoundBindHandler();
        if (annotation.ignoreInvalidFields()) {
            handler = new IgnoreErrorsBindHandler(handler);
        }
        if (!annotation.ignoreUnknownFields()) {
            UnboundElementsSourceFilter filter = new UnboundElementsSourceFilter();
            handler = new NoUnboundElementsBindHandler(handler, filter);
        }
        if (!validators.isEmpty()) {
            handler = new ValidationBindHandler(handler,
                    validators.toArray(new Validator[0]));
        }
        for (ConfigurationPropertiesBindHandlerAdvisor advisor : getBindHandlerAdvisors()) {
            handler = advisor.apply(handler);
        }
        return handler;
    }

    private List<ConfigurationPropertiesBindHandlerAdvisor> getBindHandlerAdvisors() {
//        return this.applicationContext
//                .getBeanProvider(ConfigurationPropertiesBindHandlerAdvisor.class)
//                .orderedStream().collect(Collectors.toList());

        return this.applicationContext.getBeansOfType(ConfigurationPropertiesBindHandlerAdvisor.class)
                .values().stream().collect(Collectors.toList());
    }

    private Binder getBinder() {
        if (this.binder == null) {
            this.binder = new Binder(getConfigurationPropertySources(),
                    getPropertySourcesPlaceholdersResolver(), getConversionService(),
                    getPropertyEditorInitializer());
        }
        return this.binder;
    }

    private Iterable<ConfigurationPropertySource> getConfigurationPropertySources() {
        return ConfigurationPropertySources.from(this.propertySources);
    }

    private PropertySourcesPlaceholdersResolver getPropertySourcesPlaceholdersResolver() {
        return new PropertySourcesPlaceholdersResolver(this.propertySources);
    }

    private ConversionService getConversionService() {
        return new ConversionServiceDeducer(this.applicationContext)
                .getConversionService();
    }

    private Consumer<PropertyEditorRegistry> getPropertyEditorInitializer() {
        if (this.applicationContext instanceof ConfigurableApplicationContext) {
            return ((ConfigurableApplicationContext) this.applicationContext)
                    .getBeanFactory()::copyRegisteredEditorsTo;
        }
        return null;
    }

}
