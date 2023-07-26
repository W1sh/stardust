package com.w1sh.aperture;

import com.w1sh.aperture.annotation.Provide;

@Provide
public class JakartaPreDestroyInterceptor implements InvocationInterceptor {

    @Override
    public void intercept(Object instance) {

    }

    @Override
    public InvocationType getInterceptorType() {
        return InvocationType.PRE_DESTROY;
    }
}
