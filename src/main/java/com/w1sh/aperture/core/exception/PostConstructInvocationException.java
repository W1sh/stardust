package com.w1sh.aperture.core.exception;

import java.lang.reflect.Method;

public class PostConstructInvocationException extends RuntimeException {

    public PostConstructInvocationException(Method m, Throwable e) {
        super(String.format("Can't invoke @PostConstruct annotated method %s:%s", m.getClass(), m.getName()), e);
    }
}
