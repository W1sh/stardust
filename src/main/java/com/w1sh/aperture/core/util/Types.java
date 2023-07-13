package com.w1sh.aperture.core.util;

import java.lang.reflect.ParameterizedType;

public class Types {

    private Types() {}

    public static Class<?> getActualTypeArgument(Class<?> clazz, int index) {
        return (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[index];
    }
}
