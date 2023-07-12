package com.w1sh.aperture.web.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryParam {

    String value() default "";

    boolean required() default true;

    String defaultValue() default "";
}
