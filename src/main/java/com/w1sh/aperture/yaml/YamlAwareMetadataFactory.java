package com.w1sh.aperture.yaml;

import com.w1sh.aperture.Metadata;

import java.lang.reflect.Method;

public interface YamlAwareMetadataFactory {

    Metadata create(Method method);

    Metadata create(Class<?> clazz);

}