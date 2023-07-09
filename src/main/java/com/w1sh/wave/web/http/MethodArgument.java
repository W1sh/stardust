package com.w1sh.wave.web.http;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public record MethodArgument(Method method, int index, String[] value, Class<?> expectedType) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodArgument that = (MethodArgument) o;
        return index == that.index && Objects.equals(method, that.method) && Arrays.equals(value, that.value) && Objects.equals(expectedType, that.expectedType);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(method, index, expectedType);
        result = 31 * result + Arrays.hashCode(value);
        return result;
    }

    @Override
    public String toString() {
        return "MethodArgument{" +
                "method=" + method +
                ", index=" + index +
                ", value=" + Arrays.toString(value) +
                ", expectedType=" + expectedType +
                '}';
    }
}
