package com.w1sh.wave.web.endpoint;

import com.w1sh.wave.web.exception.EndpointMethodInvocationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class HttpRequestAwareEndpoint extends Endpoint {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestAwareEndpoint.class);

    protected HttpRequestAwareEndpoint(InvocationContext context) {
        super(context);
    }

    @Override
    public Object handle(HttpServletRequest req, HttpServletResponse resp) {
        final var method = getContext().method();
        final var instance = getContext().instance();
        try {
            if (getContext().hasArguments()) {
                return method.invoke(instance, req, resp);
            } else {
                return method.invoke(instance);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Failed to invoke endpoint method");
            throw new EndpointMethodInvocationException(e);
        }
    }
}
