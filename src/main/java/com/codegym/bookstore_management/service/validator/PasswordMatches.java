package com.codegym.bookstore_management.service.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {
    String message() default "Mật khẩu xác nhận không khớp";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
