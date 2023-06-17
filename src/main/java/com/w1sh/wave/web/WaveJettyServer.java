package com.w1sh.wave.web;

import com.w1sh.wave.core.builder.ContextGroup;
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
    private static final int DEFAULT_IDLE_TIMEOUT = 8080;

    private final Server server;
    private final DispatcherServlet dispatcher;

    public WaveJettyServer(DispatcherServlet dispatcher) {
        this.server = new Server();
        this.dispatcher = dispatcher;
    }

    public void start() {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(DEFAULT_PORT);
        connector.setIdleTimeout(DEFAULT_IDLE_TIMEOUT);
        server.addConnector(connector);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        server.setHandler(contextHandler);
        contextHandler.addServlet(new ServletHolder(dispatcher), "/");
    }

    public void context(ContextGroup contextGroup) {
        RouteBuilder.setStaticContext(this);
        contextGroup.apply();
        RouteBuilder.clearStaticContext();
    }

    public void registerRoute(Route route) {
        this.dispatcher.addRoute(route);
    }
}
