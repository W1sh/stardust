package com.w1sh.stardust.dependency;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Resolver(SystemPropertyDependencyResolver.class)
public @interface DependsOnSystemProperty {

    String[] value() default {};

    String[] expectedValue() default "";
}
