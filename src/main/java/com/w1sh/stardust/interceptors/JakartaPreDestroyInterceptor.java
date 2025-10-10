package com.w1sh.stardust.interceptors;

public record JakartaPreDestroyInterceptor() implements InvocationInterceptor {

    @Override
    public void intercept(Object instance) {

    }

    @Override
    public InvocationType getInterceptorType() {
        return InvocationType.PRE_DESTROY;
    }
}
