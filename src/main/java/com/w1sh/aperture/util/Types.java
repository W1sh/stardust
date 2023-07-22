package com.w1sh.aperture.util;

import java.lang.reflect.ParameterizedType;

public class Types {

    private Types() {}

    public static Class<?> getInterfaceActualTypeArgument(Class<?> clazz, int index) {
        return (Class<?>) ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[index];
    }
}
