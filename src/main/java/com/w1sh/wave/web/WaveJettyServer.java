package com.w1sh.wave.web;

import com.w1sh.wave.core.builder.ContextGroup;
import com.w1sh.wave.web.handler.Handlers;
import com.w1sh.wave.web.routing.Route;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaveJettyServer {

    private static final Logger logger = LoggerFactory.getLogger(WaveJettyServer.class);
    private static final int DEFAULT_PORT = 8080;
    private static final int DEFAULT_IDLE_TIMEOUT = 30000;

    private final Server server;
    private final DispatcherServlet dispatcher;

    public WaveJettyServer(DispatcherServlet dispatcher) {
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
        registerRoute(new Route(HttpMethod.GET, "/wave/health", Handlers.health()));
        registerRoute(new Route(HttpMethod.GET, "/wave/shutdown", Handlers.shutdown()));
    }

    public void registerRoute(Route route) {
        this.dispatcher.addRoute(route);
    }
}
