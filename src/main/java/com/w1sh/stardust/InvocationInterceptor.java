package com.w1sh.stardust;

/**
 * Interface to be implemented by invocation interceptors.
 *
 * @see JakartaPostConstructInterceptor
 * @see JakartaPreDestroyInterceptor
 */
public interface InvocationInterceptor {

    /**
     * Intercept and handle the invocation.
     */
    void intercept(Object instance);

    /**
     * Returns the {@link InvocationType} handled by this interceptor.
     *
     * @return the {@link InvocationType} handled by this interceptor.
     */
    InvocationType getInterceptorType();

    enum InvocationType {
        /**
         * Intercepts a provider constructor invocation
         */
        POST_CONSTRUCT,

        /**
         * Intercepts a provider destroy invocation
         */
        PRE_DESTROY,
    }
}
