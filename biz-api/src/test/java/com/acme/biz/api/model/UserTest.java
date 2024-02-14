package com.acme.biz.api.model;

import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocaleContextMessageInterpolator;

import javax.validation.*;
import javax.validation.bootstrap.GenericBootstrap;
import java.util.Set;

public class UserTest {


    @Test
    public void testValidateUser() {
        GenericBootstrap genericBootstrap = Validation.byDefaultProvider();
        Configuration<?> configuration = genericBootstrap.configure();
        MessageInterpolator targetInterpolator = configuration.getDefaultMessageInterpolator();
        configuration.messageInterpolator(new LocaleContextMessageInterpolator(targetInterpolator));
        ValidatorFactory validatorFactory = configuration.buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        User user = new User();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        constraintViolations.forEach(cv ->
                System.out.println(cv.getMessage()));


    }
}
