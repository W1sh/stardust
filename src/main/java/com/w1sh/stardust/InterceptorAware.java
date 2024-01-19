package com.w1sh.stardust;

import java.util.List;

public interface InterceptorAware {

    /**
     * Add a {@link InvocationInterceptor interceptor} to be one a certain {@link InvocationInterceptor.InvocationType}.
     *
     * @param interceptor the interceptor to add
     * @see #removeInterceptor(InvocationInterceptor interceptor)
     */
    void addInterceptor(InvocationInterceptor interceptor);

    /**
     * Remove a {@link InvocationInterceptor interceptor} registered with this container.
     *
     * @param interceptor the interceptor to remove
     * @see #addInterceptor(InvocationInterceptor interceptor)
     */
    void removeInterceptor(InvocationInterceptor interceptor);

    /**
     * Remove all {@link InvocationInterceptor interceptors} registered with this container.
     */
    void removeAllInterceptors();

    List<InvocationInterceptor> getAllInterceptors();

    List<InvocationInterceptor> getAllInterceptorsOfType(InvocationInterceptor.InvocationType type);
}
