package com.w1sh.stardust;

import com.w1sh.stardust.annotation.Inject;
import com.w1sh.stardust.exception.PostConstructInvocationException;

import javax.annotation.Priority;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Priority(999)
public class SetterInjectionPostConstructInterceptor implements InvocationInterceptor {

    private final ParameterResolver resolver;

    @Inject
    public SetterInjectionPostConstructInterceptor(ParameterResolver resolver) {this.resolver = resolver;}

    @Override
    public void intercept(Object instance) {
        for (Method method : instance.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Inject.class)) {
                final var objects = new Object[method.getParameterCount()];
                Parameter[] parameters = method.getParameters();
                for (int i = 0, parametersLength = parameters.length; i < parametersLength; i++) {
                    ResolvableParameterImpl<Object> resolvableParameter = new ResolvableParameterImpl<>(parameters[i]);
                    objects[i] = resolver.resolve(resolvableParameter);
                }
                try {
                    method.invoke(instance, objects);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new PostConstructInvocationException(method, e);
                }
            }
        }
    }

    @Override
    public InvocationType getInterceptorType() {
        return InvocationType.POST_CONSTRUCT;
    }
}
