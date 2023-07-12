package com.w1sh.aperture.web.endpoint;

import com.w1sh.aperture.core.ProviderRegistry;
import com.w1sh.aperture.core.annotation.Inject;
import com.w1sh.aperture.web.http.Handler;
import com.w1sh.aperture.web.http.MethodArgumentTypeResolver;
import com.w1sh.aperture.web.http.QueryParameterMethodArgumentResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

public class EndpointFactory {

    private final ProviderRegistry registry;
    private final QueryParameterMethodArgumentResolver argumentResolver;
    private final MethodArgumentTypeResolver typeResolver;

    @Inject
    public EndpointFactory(ProviderRegistry registry, QueryParameterMethodArgumentResolver argumentResolver,
                           MethodArgumentTypeResolver typeResolver) {
        this.registry = registry;
        this.argumentResolver = argumentResolver;
        this.typeResolver = typeResolver;
    }

    public static Endpoint fromHandler(Handler handler) {
        return new Endpoint() {
            @Override
            public Object handle(HttpServletRequest request, HttpServletResponse response) {
                return handler.handle(request, response);
            }
        };
    }

    public Endpoint fromMethod(Method method, Class<?> clazz) {
        final var classInstance = registry.instance(clazz);
        if (hasNoArguments(method)) {
            return new HttpRequestAwareEndpoint(new InvocationContext(method, classInstance, false));
        } else if (hasHttpArguments(method)) {
            return new HttpRequestAwareEndpoint(new InvocationContext(method, classInstance, true));
        } else {
            return new QueryParameterAwareEndpoint(new InvocationContext(method, classInstance, true),
                    argumentResolver, typeResolver);
        }
    }

    private boolean hasNoArguments(Method method) {
        return method.getParameters().length == 0;
    }

    private boolean hasHttpArguments(Method method) {
        if (method.getParameters().length > 2) return false;
        return Arrays.stream(method.getParameterTypes())
                .allMatch(clazz -> clazz.isAssignableFrom(HttpServletRequest.class) || clazz.isAssignableFrom(HttpServletResponse.class));
    }
}
