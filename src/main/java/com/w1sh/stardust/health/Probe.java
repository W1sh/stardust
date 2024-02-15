package com.w1sh.stardust.health;

import com.w1sh.stardust.annotation.Provide;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Provide
public @interface Probe {

    int delay() default 1;

    int period() default 1;
}
