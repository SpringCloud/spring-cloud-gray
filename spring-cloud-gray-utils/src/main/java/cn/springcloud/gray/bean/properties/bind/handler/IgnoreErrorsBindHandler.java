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

package cn.springcloud.gray.bean.properties.bind.handler;

import cn.springcloud.gray.bean.properties.bind.AbstractBindHandler;
import cn.springcloud.gray.bean.properties.bind.BindContext;
import cn.springcloud.gray.bean.properties.bind.BindHandler;
import cn.springcloud.gray.bean.properties.bind.Bindable;
import cn.springcloud.gray.bean.properties.source.ConfigurationPropertyName;

/**
 * {@link BindHandler} that can be used to ignore binding errors.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 * @since 2.0.0
 */
public class IgnoreErrorsBindHandler extends AbstractBindHandler {

    public IgnoreErrorsBindHandler() {
    }

    public IgnoreErrorsBindHandler(BindHandler parent) {
        super(parent);
    }

    @Override
    public Object onFailure(ConfigurationPropertyName name, Bindable<?> target,
                            BindContext context, Exception error) throws Exception {
        return (target.getValue() != null) ? target.getValue().get() : null;
    }

}
