package com.w1sh.wave.web;

import com.w1sh.wave.web.exception.NoMatchingPathFound;
import com.w1sh.wave.web.routing.Route;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

public class DispatcherServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key";
    private final EnumMap<HttpMethod, Set<Route>> routes = new EnumMap<>(HttpMethod.class);

    public void addRoute(Route route) {
        var existingMethodRoutes = routes.get(route.method());
        if (existingMethodRoutes == null) existingMethodRoutes = new HashSet<>();
        existingMethodRoutes.add(route);
        routes.put(route.method(), existingMethodRoutes);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getHeader(SEC_WEBSOCKET_KEY) == null) {
            Route matchingRoute = findMatchingRoute(req.getMethod(), req.getRequestURI());
            logger.debug("Found matching route for method {} and path {}", req.getMethod(), req.getRequestURI());
            matchingRoute.handler().handle(req, resp);
        }
        log("Websocket requested are currently unsupported");
        super.service(req, resp);
    }

    protected Route findMatchingRoute(String method, String path) throws NoMatchingPathFound {
        HttpMethod httpMethod = HttpMethod.fromString(method);
        return routes.get(httpMethod).stream()
                .filter(route -> path.equalsIgnoreCase(route.path()))
                .findFirst()
                .orElseThrow(() -> new NoMatchingPathFound(String.format("No matching path found for method %s and path %s", method, path)));
    }
}
