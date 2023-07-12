package com.w1sh.aperture.web.endpoint;

import java.lang.reflect.Method;
import java.util.Objects;

public record InvocationContext(Method method, Object instance, boolean hasArguments) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvocationContext that = (InvocationContext) o;
        return hasArguments == that.hasArguments && Objects.equals(method, that.method) && Objects.equals(instance, that.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, instance, hasArguments);
    }

    @Override
    public String toString() {
        return "InvocationContext{" +
                "method=" + method +
                ", instance=" + instance +
                ", hasArguments=" + hasArguments +
                '}';
    }
}
