package com.w1sh.wave.util;

import com.w1sh.wave.core.ConstructorInjectionPoint;
import com.w1sh.wave.core.InjectionPoint;
import com.w1sh.wave.core.MethodInjectionPoint;
import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.core.exception.ComponentCreationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class ReflectionUtils {

    private ReflectionUtils(){}

    public static Constructor<?> findInjectAnnotatedConstructor(Class<?> aClass) {
        if (Modifier.isAbstract(aClass.getModifiers())) return null;
        for (Constructor<?> constructor : aClass.getConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                return constructor;
            }
        }
        return aClass.getConstructors()[0];
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return newInstance(clazz.getDeclaredConstructor(), new Object[]{});
        } catch (NoSuchMethodException e) {
            throw new ComponentCreationException("No declared constructor found for class", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(InjectionPoint injectionPoint, Object[] params) {
        if (injectionPoint instanceof MethodInjectionPoint) {
            return (T) newInstance(((MethodInjectionPoint) injectionPoint).getMethod(),
                    ((MethodInjectionPoint) injectionPoint).getInstanceConfigurationClass(), params);
        } else {
            return (T) newInstance(((ConstructorInjectionPoint) injectionPoint).getConstructor(), params);
        }
    }

    private static Object newInstance(Method method, Object instance, Object[] params) {
        try {
            return method.invoke(instance, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ComponentCreationException("Failed to invoke method", e);
        }
    }

    public static <T> T newInstance(Constructor<T> constructor, Object[] params) {
        try {
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ComponentCreationException("Unable to create an instance of the class", e);
        }
    }
}
