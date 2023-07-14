package com.w1sh.aperture.web.routing;

import com.w1sh.aperture.core.DefaultProviderRegistry;
import com.w1sh.aperture.example.controller.impl.CalculatorControllerImpl;
import com.w1sh.aperture.example.controller.impl.EmptyCalculatorControllerImpl;
import com.w1sh.aperture.web.endpoint.EndpointFactory;
import com.w1sh.aperture.web.http.MethodArgumentTypeResolver;
import com.w1sh.aperture.web.http.QueryParameterMethodArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.w1sh.aperture.util.Tests.register;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RouteFactoryTest {

    private RouteFactory routeFactory;
    private EndpointFactory endpointFactory;

    @BeforeEach
    void setUp() {
        DefaultProviderRegistry registry = new DefaultProviderRegistry();

        register(registry, CalculatorControllerImpl.class);
        register(registry, QueryParameterMethodArgumentResolver.class);
        register(registry, MethodArgumentTypeResolver.class);

        endpointFactory = spy(new EndpointFactory(registry, registry.instance(QueryParameterMethodArgumentResolver.class),
                registry.instance(MethodArgumentTypeResolver.class)));
        routeFactory = new RouteFactory(endpointFactory);
    }

    @Test
    void should_returnEmptySetOfRoutes_whenNoEndpointsAreDefinedInTheClass() {
        final var routes = routeFactory.fromResource(EmptyCalculatorControllerImpl.class);

        assertNotNull(routes);
        assertEquals(0, routes.size());
    }

    @Test
    void should_returnSetOfRoutes_whenEndpointsAreDefinedInTheClass() {
        final var routes = routeFactory.fromResource(CalculatorControllerImpl.class);

        assertNotNull(routes);
        assertEquals(1, routes.size());
        verify(endpointFactory, times(1)).fromMethod(any(), any());
    }
}