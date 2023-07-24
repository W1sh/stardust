package com.w1sh.aperture.yaml;

import com.w1sh.aperture.Metadata;
import com.w1sh.aperture.MetadataFactory;

import java.lang.reflect.Method;

public class YamlAwareMetadataFactory implements MetadataFactory {

    private final YamlPropertiesRegistry propertiesRegistry;

    public YamlAwareMetadataFactory(YamlPropertiesRegistry propertiesRegistry) {
        this.propertiesRegistry = propertiesRegistry;
    }

    @Override
    public Metadata create(Method method) {
        return null;
    }

    @Override
    public Metadata create(Class<?> clazz) {
        return null;
    }
}