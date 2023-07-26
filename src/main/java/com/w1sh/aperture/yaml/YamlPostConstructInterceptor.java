package com.w1sh.aperture.yaml;

import com.w1sh.aperture.InvocationInterceptor;
import com.w1sh.aperture.annotation.Provide;

@Provide
public class YamlPostConstructInterceptor implements InvocationInterceptor {

    @Override
    public void intercept(Object instance) {

    }

    @Override
    public InvocationType getInterceptorType() {
        return InvocationType.POST_CONSTRUCT;
    }
}
