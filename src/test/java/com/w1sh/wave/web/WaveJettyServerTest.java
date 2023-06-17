package com.w1sh.wave.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.w1sh.wave.web.RouteBuilder.*;
import static org.mockito.ArgumentMatchers.any;

class WaveJettyServerTest {

    private WaveJettyServer waveJettyServer;
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = Mockito.spy(new DispatcherServlet());
        waveJettyServer = new WaveJettyServer(dispatcherServlet);
    }

    @Test
    void should_registerRoute_whenCalledWithRequiredParameters() {
        waveJettyServer.context(() -> {
            get("/hello", (request, response) -> "Hello World!");
            put("/hello", (request, response) -> "Hello World!");
            patch("/hello", (request, response) -> "Hello World!");
            post("/hello", (request, response) -> "Hello World!");
            delete("/hello", (request, response) -> "Hello World!");
        });

        Mockito.verify(dispatcherServlet, Mockito.times(5)).addRoute(any());
    }

    @Test
    void should_registerDefaultHandlers_whenContextIsInitialized() {
        waveJettyServer.context(() -> {});

        Mockito.verify(dispatcherServlet, Mockito.times(2)).addRoute(any());
    }
}