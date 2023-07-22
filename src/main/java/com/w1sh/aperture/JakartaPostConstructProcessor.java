package com.w1sh.aperture;

import com.w1sh.aperture.exception.PostConstructInvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Deque;
import java.util.LinkedList;

public class JakartaPostConstructProcessor implements PostConstructorProcessor {

    private static final Logger logger = LoggerFactory.getLogger(JakartaPostConstructProcessor.class);

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
        logger.debug("Found {} post construct methods to invoke on class {}", postConstructMethods.size(), instance.getClass());
        for (Method m : postConstructMethods) {
            try {
                m.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new PostConstructInvocationException(m, e);
            }
        }
    }
}
