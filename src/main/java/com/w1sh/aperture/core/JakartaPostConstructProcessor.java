package com.w1sh.aperture.core;

import com.w1sh.aperture.core.exception.PostConstructInvocationException;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Deque;
import java.util.LinkedList;

public class JakartaPostConstructProcessor implements PostConstructorProcessor {

    @Override
    public void process(Object instance) {
        final Deque<Method> postConstructMethods = new LinkedList<>();
        for (Class<?> clazz = instance.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(PostConstruct.class)) {
                    postConstructMethods.addFirst(method);
                }
            }
        }

        for (Method m : postConstructMethods) {
            try {
                m.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new PostConstructInvocationException(m, e);
            }
        }
    }
}
