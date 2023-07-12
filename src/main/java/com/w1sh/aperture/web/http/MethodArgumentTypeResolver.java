package com.w1sh.aperture.web.http;

import com.w1sh.aperture.web.annotation.QueryParam;
import com.w1sh.aperture.web.exception.TypeMatchingException;
import com.w1sh.aperture.web.http.mapper.ValueMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MethodArgumentTypeResolver {

    public Object[] resolveMethodArguments(List<MethodArgument> arguments) {
        Object[] resolvedArguments = new Object[arguments.size()];
        for (int i = 0, argumentsSize = arguments.size(); i < argumentsSize; i++) {
            resolvedArguments[i] = resolveType(arguments.get(i));
        }
        return resolvedArguments;
    }

    public Object resolveType(MethodArgument argument) {
        if (argument.value() != null) {
            if (Object.class == argument.expectedType()) {
                return argument.value();
            } else if (argument.expectedType().isArray()) {
                throw new TypeMatchingException("Unsupported type resolution");
            } else if (argument.expectedType().isAssignableFrom(Collection.class)) {
                throw new TypeMatchingException("Unsupported type resolution");
            } else if (argument.expectedType().isAssignableFrom(Map.class)) {
                throw new TypeMatchingException("Unsupported type resolution");
            } else {
                if (argument.value().length > 1) {
                    throw new TypeMatchingException("Expected single value but received multiple");
                } else {
                    return resolveOrDefault(argument);
                }
            }
        }
        return null;
    }

    private Object resolveOrDefault(MethodArgument argument) {
        QueryParam annotation = argument.method().getParameters()[argument.index()].getAnnotation(QueryParam.class);
        if (argument.value()[0].isBlank() && annotation != null && !annotation.defaultValue().isBlank()) {
            return ValueMapper.fromString(annotation.defaultValue(), argument.expectedType());
        } else {
            return ValueMapper.fromString(argument.value()[0], argument.expectedType());
        }
    }
}
