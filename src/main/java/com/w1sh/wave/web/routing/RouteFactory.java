package com.w1sh.wave.web.routing;

import com.w1sh.wave.core.WaveContext;
import com.w1sh.wave.web.exception.EndpointMethodInvocationException;
import com.w1sh.wave.web.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.w1sh.wave.web.HttpMethod.UNSUPPORTED;
import static com.w1sh.wave.web.HttpMethod.fromString;

public class RouteFactory {

    private static final Logger logger = LoggerFactory.getLogger(RouteFactory.class);

    private final WaveContext waveContext;

    public RouteFactory(WaveContext waveContext) {
        this.waveContext = waveContext;
    }

    public Set<Route> fromResource(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Path.class)) {
            logger.error("Resource class {} does not contain a path mapping value. " +
                    "Please use Path annotation to define a context path for this resource", clazz.getSimpleName());
            return new HashSet<>();
        }
        return extractEndpoints(clazz);
    }

    private Set<Route> extractEndpoints(Class<?> clazz) {
        final var path = clazz.getAnnotation(Path.class).value();
        final var routes = new HashSet<Route>();
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            final var method = Arrays.stream(declaredMethod.getDeclaredAnnotations())
                    .filter(annotation -> annotation.annotationType().isAnnotationPresent(HttpMethod.class))
                    .map(annotation -> fromString(annotation.annotationType().getSimpleName()))
                    .findFirst()
                    .orElse(UNSUPPORTED);
            if (!UNSUPPORTED.equals(method)) {
                routes.add(new Route(method, path, createEndpointHandler(declaredMethod, clazz)));
            }
        }
        return routes;
    }

    private Handler createEndpointHandler(Method method, Class<?> clazz) {
        final var classInstance = waveContext.instance(clazz);
        return (req, resp) -> {
            try {
                return method.invoke(classInstance, req, resp);
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("Failed to invoke endpoint method");
                throw new EndpointMethodInvocationException(e);
            }
        };
    }
}
