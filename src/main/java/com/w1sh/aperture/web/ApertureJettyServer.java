package com.w1sh.aperture.web;

import com.w1sh.aperture.core.annotation.Inject;
import com.w1sh.aperture.core.builder.ContextGroup;
import com.w1sh.aperture.web.endpoint.core.HealthEndpoint;
import com.w1sh.aperture.web.endpoint.core.ShutdownEndpoint;
import com.w1sh.aperture.web.routing.Route;
import com.w1sh.aperture.web.routing.RouteBuilder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApertureJettyServer {

    private static final Logger logger = LoggerFactory.getLogger(ApertureJettyServer.class);
    private static final int DEFAULT_PORT = 8080;
    private static final int DEFAULT_IDLE_TIMEOUT = 30000;

    private final Server server;
    private final DispatcherServlet dispatcher;

    @Inject
    public ApertureJettyServer(DispatcherServlet dispatcher) {
        this.server = new Server();
        this.dispatcher = dispatcher;
    }

    public void start() throws Exception {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(DEFAULT_PORT);
        connector.setIdleTimeout(DEFAULT_IDLE_TIMEOUT);
        server.addConnector(connector);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        contextHandler.addServlet(new ServletHolder(dispatcher), "/");
        server.setHandler(contextHandler);
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public void context(ContextGroup contextGroup) {
        RouteBuilder.setStaticContext(this);
        if (contextGroup != null) contextGroup.apply();
        registerDefaultHandlers();
        RouteBuilder.clearStaticContext();
    }

    private void registerDefaultHandlers() {
        logger.info("Registering default handlers");
        registerRoute(new Route(HttpMethod.GET, "/aperture/health", new HealthEndpoint()));
        registerRoute(new Route(HttpMethod.GET, "/aperture/shutdown", new ShutdownEndpoint()));
    }

    public void registerRoute(Route route) {
        this.dispatcher.addRoute(route);
    }
}
