package com.w1sh.wave.web.routing;

import com.w1sh.wave.core.WaveContext;
import com.w1sh.wave.example.controller.impl.CalculatorControllerImpl;
import com.w1sh.wave.example.controller.impl.EmptyCalculatorControllerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.w1sh.wave.core.builder.ContextBuilder.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RouteFactoryTest {

    private WaveContext waveContext;
    private RouteFactory routeFactory;

    @BeforeEach
    void setUp() {
        waveContext = spy(new WaveContext());
        waveContext.context(() -> singleton(CalculatorControllerImpl.class));

        routeFactory = new RouteFactory(waveContext);
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
        verify(waveContext, times(1)).instance(CalculatorControllerImpl.class);
    }
}