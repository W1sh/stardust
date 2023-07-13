package com.w1sh.aperture.web.endpoint;

import com.w1sh.aperture.core.ProviderRegistry;
import com.w1sh.aperture.example.controller.impl.CalculatorControllerImpl;
import com.w1sh.aperture.web.DispatcherServlet;
import com.w1sh.aperture.web.ApertureJettyServer;
import com.w1sh.aperture.web.http.MethodArgumentTypeResolver;
import com.w1sh.aperture.web.http.QueryParameterMethodArgumentResolver;
import com.w1sh.aperture.web.routing.Route;
import com.w1sh.aperture.web.routing.RouteFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.w1sh.aperture.util.Tests.register;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EndpointDiscovererTest {

    private EndpointDiscoverer endpointDiscoverer;
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        ProviderRegistry registry = new ProviderRegistry();

        register(registry, CalculatorControllerImpl.class);
        register(registry, QueryParameterMethodArgumentResolver.class);
        register(registry, MethodArgumentTypeResolver.class);
        register(registry, EndpointFactory.class);
        register(registry, RouteFactory.class);
        register(registry, DispatcherServlet.class);
        register(registry, ApertureJettyServer.class);

        dispatcherServlet = registry.instance(DispatcherServlet.class);
        endpointDiscoverer = new EndpointDiscoverer(registry, registry.instance(RouteFactory.class), registry.instance(ApertureJettyServer.class));
    }

    @Test
    void should_returnDiscoverAndRegisterRoutes_whenContextIsPresentAndEndpointsExist() {
        endpointDiscoverer.discover();

        List<Route> allRoutes = dispatcherServlet.getAllRoutes();

        assertNotNull(allRoutes);
        assertEquals(1, allRoutes.size());
    }
}