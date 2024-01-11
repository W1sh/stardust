package com.w1sh.aperture.yaml;

import com.w1sh.aperture.InvocationInterceptor;
import com.w1sh.aperture.annotation.Provide;

import java.util.TreeMap;

@Provide
public class YamlPostConstructInterceptor implements InvocationInterceptor {

    private final YamlPropertiesRegistry yamlPropertiesRegistry;

    public YamlPostConstructInterceptor(YamlPropertiesRegistry yamlPropertiesRegistry) {
        this.yamlPropertiesRegistry = yamlPropertiesRegistry;
    }

    @Override
    public void intercept(Object instance) {

    }

    @Override
    public InvocationType getInterceptorType() {
        return InvocationType.POST_CONSTRUCT;
    }
}
