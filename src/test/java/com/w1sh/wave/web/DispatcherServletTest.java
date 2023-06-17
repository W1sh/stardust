package com.w1sh.wave.web;

import com.w1sh.wave.web.exception.NoMatchingPathFound;
import com.w1sh.wave.web.routing.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
    }

    @Test
    void should_findMatchingRoute_whenRouteWithProvidedMethodAndPathIsRegistered() throws NoMatchingPathFound {
        Route route = new Route(HttpMethod.GET, "/hello", (request, response) -> "Hello World!");

        dispatcherServlet.addRoute(route);
        Route matchingRoute = dispatcherServlet.findMatchingRoute("get", "/hello");

        assertNotNull(matchingRoute);
        assertEquals(HttpMethod.GET, matchingRoute.method());
        assertEquals("/hello", matchingRoute.path());
    }

    @Test
    void should_throwNoMatchingPathFoundException_whenRouteWithProvidedMethodAndPathIsNotRegistered() {
        Route route = new Route(HttpMethod.GET, "/hello", (request, response) -> "Hello World!");

        dispatcherServlet.addRoute(route);
        assertThrows(NoMatchingPathFound.class, () -> dispatcherServlet.findMatchingRoute("get", "/byebye"));
    }
}