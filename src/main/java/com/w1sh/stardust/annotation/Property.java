package com.w1sh.stardust.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Property {

    String value();

    boolean isArray() default false;

    String arraySeparator() default ",";
}
