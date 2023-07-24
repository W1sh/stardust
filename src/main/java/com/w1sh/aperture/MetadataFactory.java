package com.w1sh.aperture;

import java.lang.reflect.Method;

public interface MetadataFactory {

    Metadata create(Method method);

    Metadata create(Class<?> clazz);
}
