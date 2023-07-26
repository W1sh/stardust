package com.w1sh.aperture.annotation;

import com.w1sh.aperture.annotation.Provide;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Provide
public @interface Module {

}
