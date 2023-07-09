package com.w1sh.wave.web;

import com.w1sh.wave.web.endpoint.Endpoint;
import com.w1sh.wave.web.exception.RouteMatchingException;
import com.w1sh.wave.web.routing.Route;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    void should_findMatchingRoute_whenRouteWithProvidedMethodAndPathIsRegistered() throws RouteMatchingException {
        Route route = new Route(HttpMethod.GET, "/hello", simpleEndpoint());

        dispatcherServlet.addRoute(route);
        Route matchingRoute = dispatcherServlet.findMatchingRoute("get", "/hello");

        assertNotNull(matchingRoute);
        assertEquals(HttpMethod.GET, matchingRoute.method());
        assertEquals("/hello", matchingRoute.path());
    }

    @Test
    void should_throwNoMatchingPathFoundException_whenRouteWithProvidedMethodAndPathIsNotRegistered() {
        Route route = new Route(HttpMethod.GET, "/hello", simpleEndpoint());

        dispatcherServlet.addRoute(route);
        assertThrows(RouteMatchingException.class, () -> dispatcherServlet.findMatchingRoute("get", "/byebye"));
    }

    private Endpoint simpleEndpoint() {
        return new Endpoint() {
            @Override
            public Object handle(HttpServletRequest request, HttpServletResponse response) {
                return  "Hello World!";
            }
        };
    }
}