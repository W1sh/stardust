package com.w1sh.aperture.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.w1sh.aperture.web.routing.RouteBuilder.*;
import static org.mockito.ArgumentMatchers.any;

class ApertureJettyServerTest {

    private ApertureJettyServer apertureJettyServer;
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() throws Exception {
        dispatcherServlet = Mockito.spy(new DispatcherServlet());
        apertureJettyServer = new ApertureJettyServer(dispatcherServlet);
        apertureJettyServer.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        apertureJettyServer.stop();
    }

    @Test
    void should_registerRoute_whenCalledWithRequiredParameters() {
        apertureJettyServer.context(() -> {
            get("/hello", (request, response) -> "Hello World!");
            put("/hello", (request, response) -> "Hello World!");
            patch("/hello", (request, response) -> "Hello World!");
            post("/hello", (request, response) -> "Hello World!");
            delete("/hello", (request, response) -> "Hello World!");
        });

        // 5 user-defined + 2 default handlers
        Mockito.verify(dispatcherServlet, Mockito.times(7)).addRoute(any());
    }

    @Test
    void should_registerDefaultHandlers_whenContextIsInitialized() {
        apertureJettyServer.context(null);

        Mockito.verify(dispatcherServlet, Mockito.times(2)).addRoute(any());
    }
}