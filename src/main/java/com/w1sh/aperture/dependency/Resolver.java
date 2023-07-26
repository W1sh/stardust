package com.w1sh.aperture.dependency;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Resolver {

    Class<? extends DependencyResolver> value();
}
