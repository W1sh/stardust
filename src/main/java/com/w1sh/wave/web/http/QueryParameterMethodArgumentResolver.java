package com.w1sh.wave.web.http;

import com.w1sh.wave.web.annotation.QueryParam;
import com.w1sh.wave.web.exception.RequiredArgumentException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class QueryParameterMethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(QueryParameterMethodArgumentResolver.class);

    public List<MethodArgument> resolveQueryParameters(HttpServletRequest request, Method method) throws RequiredArgumentException {
        final var parameterMap = request.getParameterMap();
        final var methodParameters = method.getParameters();
        final var methodArguments = new ArrayList<MethodArgument>();

        for (int i = 0, parametersLength = methodParameters.length; i < parametersLength; i++) {
            Parameter methodParameter = methodParameters[i];
            if (parameterMap.containsKey(methodParameter.getName())) {
                final var value = parameterMap.get(methodParameter.getName());
                methodArguments.add(new MethodArgument(method, i, value, methodParameter.getType()));
            } else {
                QueryParam annotation = methodParameter.getAnnotation(QueryParam.class);
                if (annotation != null && annotation.required()) {
                    throw new RequiredArgumentException(String.format("Parameter %s is required but it is not present", methodParameter.getName()));
                }
                logger.warn("No value found for method parameter {}", methodParameter.getName());
                methodArguments.add(new MethodArgument(method, i, null, null));
            }
            parameterMap.remove(methodParameter.getName());
        }

        if (!parameterMap.isEmpty()) {
            parameterMap.forEach((key, value) -> logger.warn("Argument {} received but it is not defined", key));
        }
        return methodArguments;
    }
}