package com.w1sh.stardust.annotation;

import com.w1sh.stardust.dependency.ActiveProfileDependencyResolver;
import com.w1sh.stardust.dependency.Resolver;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
@Documented
@Resolver(ActiveProfileDependencyResolver.class)
public @interface Profile {

    String[] value();
}
