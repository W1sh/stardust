package com.w1sh.stardust.util;

import javax.annotation.Priority;
import java.lang.reflect.ParameterizedType;

public class Types {

    private Types() {}

    public static Class<?> getInterfaceActualTypeArgument(Class<?> clazz, int index) {
        return (Class<?>) ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[index];
    }

    public static Integer getPriority(Class<?> clazz) {
        Priority annotation = clazz.getAnnotation(Priority.class);
        return annotation != null ? annotation.value() : 0;
    }
}
