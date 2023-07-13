package com.w1sh.aperture.web.endpoint;

import com.w1sh.aperture.core.ProviderRegistry;
import com.w1sh.aperture.web.ApertureJettyServer;
import com.w1sh.aperture.web.routing.Route;
import com.w1sh.aperture.web.routing.RouteFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.Path;
import java.util.List;
import java.util.Set;

public class EndpointDiscoverer {

    private static final Logger logger = LoggerFactory.getLogger(EndpointDiscoverer.class);

    private final ProviderRegistry registry;
    private final RouteFactory routeFactory;
    private final ApertureJettyServer server;

    public EndpointDiscoverer(ProviderRegistry registry, RouteFactory routeFactory, ApertureJettyServer apertureJettyServer) {
        this.registry = registry;
        this.routeFactory = routeFactory;
        this.server = apertureJettyServer;
    }

    public void discover() {
        var routesRegistered = 0;
        List<Class<?>> classesAnnotatedWithPath = registry.getAllAnnotatedWith(Path.class);
        for (Class<?> clazz : classesAnnotatedWithPath) {
            logger.info("Searching class {} for defined endpoints", clazz.getSimpleName());
            Set<Route> routes = routeFactory.fromResource(clazz);
            logger.info("Found {} endpoints on class {}", routes.size(), clazz.getSimpleName());
            routes.forEach(server::registerRoute);
            routesRegistered += routes.size();
        }
        logger.info("Endpoint discovery complete. Found and registered {} endpoints", routesRegistered);
    }
}
