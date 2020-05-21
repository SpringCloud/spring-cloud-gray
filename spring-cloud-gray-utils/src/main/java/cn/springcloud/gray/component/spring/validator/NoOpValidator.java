package cn.springcloud.gray.component.spring.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author saleson
 * @date 2020-05-17 23:32
 */
public class NoOpValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
