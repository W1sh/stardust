package com.w1sh.aperture.configuration;

import com.w1sh.aperture.InvocationInterceptor;
import com.w1sh.aperture.annotation.Provide;
import com.w1sh.aperture.configuration.PropertiesRegistry;

@Provide
public class YamlPostConstructInterceptor implements InvocationInterceptor {

    private final PropertiesRegistry yamlPropertiesRegistry;

    public YamlPostConstructInterceptor(PropertiesRegistry yamlPropertiesRegistry) {
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
