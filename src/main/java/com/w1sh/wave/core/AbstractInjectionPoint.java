package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Qualifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

public abstract class AbstractInjectionPoint<T> {

    private final Constructor<T> constructor;
    private Qualifier[] qualifiers;
    private Type[] parameterTypes;

    protected AbstractInjectionPoint(Constructor<T> constructor) {
        this.constructor = constructor;
    }

    public abstract T create(Object[] params);

    public Constructor<T> getConstructor() {
        return constructor;
    }

    public Qualifier[] getQualifiers() {
        return qualifiers != null ? qualifiers : new Qualifier[0];
    }

    public void setQualifiers(Qualifier[] qualifiers) {
        this.qualifiers = qualifiers;
    }

    public Type[] getParameterTypes() {
        return parameterTypes != null ? parameterTypes : new Type[0];
    }

    public void setParameterTypes(Type[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}