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

package cn.springcloud.gray.bean.properties.bind.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.springcloud.gray.bean.properties.source.ConfigurationProperty;
import cn.springcloud.gray.bean.properties.source.ConfigurationPropertyName;
import cn.springcloud.gray.bean.properties.source.ConfigurationPropertyName.Form;
import org.springframework.util.Assert;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * A collection of {@link ObjectError ObjectErrors} caused by bind validation failures.
 * Where possible, included {@link FieldError FieldErrors} will be OriginProvider.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 * @since 2.0.0
 */
public class ValidationErrors implements Iterable<ObjectError> {

    private final ConfigurationPropertyName name;

    private final Set<ConfigurationProperty> boundProperties;

    private final List<ObjectError> errors;

    ValidationErrors(ConfigurationPropertyName name,
                     Set<ConfigurationProperty> boundProperties, List<ObjectError> errors) {
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(boundProperties, "BoundProperties must not be null");
        Assert.notNull(errors, "Errors must not be null");
        this.name = name;
        this.boundProperties = Collections.unmodifiableSet(boundProperties);
        this.errors = convertErrors(name, boundProperties, errors);
    }

    private List<ObjectError> convertErrors(ConfigurationPropertyName name,
                                            Set<ConfigurationProperty> boundProperties, List<ObjectError> errors) {
        List<ObjectError> converted = new ArrayList<>(errors.size());
        for (ObjectError error : errors) {
            converted.add(convertError(name, boundProperties, error));
        }
        return Collections.unmodifiableList(converted);
    }

    private ObjectError convertError(ConfigurationPropertyName name,
                                     Set<ConfigurationProperty> boundProperties, ObjectError error) {
        if (error instanceof FieldError) {
            return convertFieldError(name, boundProperties, (FieldError) error);
        }
        return error;
    }

    private FieldError convertFieldError(ConfigurationPropertyName name,
                                         Set<ConfigurationProperty> boundProperties, FieldError error) {
        return OriginTrackedFieldError.of(error);
    }


    private boolean isForError(ConfigurationPropertyName name,
                               ConfigurationPropertyName boundPropertyName, FieldError error) {
        return name.isParentOf(boundPropertyName) && boundPropertyName
                .getLastElement(Form.UNIFORM).equalsIgnoreCase(error.getField());
    }

    /**
     * Return the name of the item that was being validated.
     *
     * @return the name of the item
     */
    public ConfigurationPropertyName getName() {
        return this.name;
    }

    /**
     * Return the properties that were bound before validation failed.
     *
     * @return the boundProperties
     */
    public Set<ConfigurationProperty> getBoundProperties() {
        return this.boundProperties;
    }

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    /**
     * Return the list of all validation errors.
     *
     * @return the errors
     */
    public List<ObjectError> getAllErrors() {
        return this.errors;
    }

    @Override
    public Iterator<ObjectError> iterator() {
        return this.errors.iterator();
    }

}
