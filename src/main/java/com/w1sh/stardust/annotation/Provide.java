package com.w1sh.stardust.annotation;

import com.w1sh.stardust.Scope;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Provide {

    String value() default "";

    Scope scope() default Scope.SINGLETON;
}
