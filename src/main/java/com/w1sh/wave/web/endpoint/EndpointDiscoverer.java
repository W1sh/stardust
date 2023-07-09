package com.w1sh.wave.web.endpoint;

import com.w1sh.wave.core.WaveContext;
import com.w1sh.wave.web.DispatcherServlet;
import com.w1sh.wave.web.WaveJettyServer;
import com.w1sh.wave.web.routing.Route;
import com.w1sh.wave.web.routing.RouteFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;
import java.util.List;
import java.util.Set;

public class EndpointDiscoverer {

    private static final Logger logger = LoggerFactory.getLogger(EndpointDiscoverer.class);

    private final WaveContext waveContext;
    private final RouteFactory routeFactory;
    private final WaveJettyServer server;

    public EndpointDiscoverer(WaveContext waveContext) {
        this.waveContext = waveContext;
        this.routeFactory = waveContext.instance(RouteFactory.class);
        this.server = waveContext.instance(WaveJettyServer.class);
    }

    public void discover() {
        var routesRegistered = 0;
        List<Class<?>> classesAnnotatedWithPath = waveContext.findRegisteredClassesAnnotatedWith(Path.class);
        for (Class<?> clazz : classesAnnotatedWithPath) {
            logger.info("Searching class {} for defined endpoints", clazz);
            Set<Route> routes = routeFactory.fromResource(clazz);
            logger.info("Found {} endpoints on class {}", routes.size(), clazz);
            routes.forEach(server::registerRoute);
            routesRegistered += routes.size();
        }
        logger.info("Endpoint discovery complete. Found and registered {} endpoints", routesRegistered);
    }
}
