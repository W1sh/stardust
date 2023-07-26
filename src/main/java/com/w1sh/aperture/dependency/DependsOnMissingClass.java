package com.w1sh.aperture.dependency;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Resolver(SystemPropertyDependencyResolver.class)
public @interface DependsOnMissingClass {

    Class<?>[] value() default {};
}
