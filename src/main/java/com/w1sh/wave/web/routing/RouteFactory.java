package com.w1sh.wave.web.routing;

import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.web.endpoint.EndpointFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.w1sh.wave.web.HttpMethod.UNSUPPORTED;
import static com.w1sh.wave.web.HttpMethod.fromString;

public class RouteFactory {

    private static final Logger logger = LoggerFactory.getLogger(RouteFactory.class);

    private final EndpointFactory endpointFactory;

    @Inject
    public RouteFactory(EndpointFactory endpointFactory) {
        this.endpointFactory = endpointFactory;
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
                routes.add(new Route(method, path, endpointFactory.fromMethod(declaredMethod, clazz)));
            }
        }
        return routes;
    }
}
