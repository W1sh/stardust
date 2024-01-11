package com.w1sh.aperture.configuration;

import com.w1sh.aperture.InvocationInterceptor;
import com.w1sh.aperture.annotation.Provide;

@Provide
public class PropertiesPostConstructInterceptor implements InvocationInterceptor {

    @Override
    public void intercept(Object instance) {

    }

    @Override
    public InvocationType getInterceptorType() {
        return InvocationType.POST_CONSTRUCT;
    }
}
