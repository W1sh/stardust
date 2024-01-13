package com.w1sh.stardust;

public class JakartaPreDestroyInterceptor implements InvocationInterceptor {

    @Override
    public void intercept(Object instance) {

    }

    @Override
    public InvocationType getInterceptorType() {
        return InvocationType.PRE_DESTROY;
    }
}
