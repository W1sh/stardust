package com.w1sh.wave.web.endpoint;

import com.w1sh.wave.web.exception.EndpointMethodInvocationException;
import com.w1sh.wave.web.exception.RequiredArgumentException;
import com.w1sh.wave.web.exception.TypeMatchingException;
import com.w1sh.wave.web.http.MethodArgument;
import com.w1sh.wave.web.http.MethodArgumentTypeResolver;
import com.w1sh.wave.web.http.QueryParameterMethodArgumentResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class QueryParameterAwareEndpoint extends Endpoint {

    private static final Logger logger = LoggerFactory.getLogger(QueryParameterAwareEndpoint.class);

    private final QueryParameterMethodArgumentResolver argumentResolver;
    private final MethodArgumentTypeResolver typeResolver;

    protected QueryParameterAwareEndpoint(InvocationContext context, QueryParameterMethodArgumentResolver argumentResolver,
                                          MethodArgumentTypeResolver typeResolver) {
        super(context);
        this.argumentResolver = argumentResolver;
        this.typeResolver = typeResolver;
    }

    @Override
    public Object handle(HttpServletRequest req, HttpServletResponse resp) {
        final var method = getContext().method();
        final var instance = getContext().instance();
        try {
            List<MethodArgument> methodArguments = argumentResolver.resolveQueryParameters(req, method);
            Object[] objects = typeResolver.resolveMethodArguments(methodArguments);
            return method.invoke(instance, objects);
        } catch (IllegalAccessException | InvocationTargetException | RequiredArgumentException e) {
            logger.error("Failed to invoke endpoint method");
            throw new EndpointMethodInvocationException(e);
        }
    }
}
