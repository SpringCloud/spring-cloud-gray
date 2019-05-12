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

import org.springframework.validation.FieldError;

/**
 * {@link FieldError} implementation that tracks the source .
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 */
final class OriginTrackedFieldError extends FieldError {


    private OriginTrackedFieldError(FieldError fieldError) {
        super(fieldError.getObjectName(), fieldError.getField(),
                fieldError.getRejectedValue(), fieldError.isBindingFailure(),
                fieldError.getCodes(), fieldError.getArguments(),
                fieldError.getDefaultMessage());
    }


    @Override
    public String toString() {
        return super.toString();
    }

    public static FieldError of(FieldError fieldError) {
        if (fieldError == null) {
            return fieldError;
        }
        return new OriginTrackedFieldError(fieldError);
    }

}
