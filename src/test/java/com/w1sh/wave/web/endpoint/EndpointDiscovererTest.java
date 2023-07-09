package com.w1sh.wave.web.endpoint;

import com.w1sh.wave.core.WaveContext;
import com.w1sh.wave.example.controller.impl.CalculatorControllerImpl;
import com.w1sh.wave.web.DispatcherServlet;
import com.w1sh.wave.web.WaveJettyServer;
import com.w1sh.wave.web.http.MethodArgumentTypeResolver;
import com.w1sh.wave.web.http.QueryParameterMethodArgumentResolver;
import com.w1sh.wave.web.routing.Route;
import com.w1sh.wave.web.routing.RouteFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.w1sh.wave.core.builder.ContextBuilder.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.spy;

class EndpointDiscovererTest {

    private EndpointDiscoverer endpointDiscoverer;
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        WaveContext waveContext = spy(new WaveContext());
        waveContext.context(() -> {
            singleton(CalculatorControllerImpl.class);
            singleton(QueryParameterMethodArgumentResolver.class);
            singleton(MethodArgumentTypeResolver.class);
            singleton(EndpointFactory.class);
            singleton(RouteFactory.class);
            singleton(DispatcherServlet.class);
            singleton(WaveJettyServer.class);
        });

        dispatcherServlet = waveContext.instance(DispatcherServlet.class);
        endpointDiscoverer = new EndpointDiscoverer(waveContext);
    }

    @Test
    void should_returnDiscoverAndRegisterRoutes_whenContextIsPresentAndEndpointsExist() {
        endpointDiscoverer.discover();

        List<Route> allRoutes = dispatcherServlet.getAllRoutes();

        assertNotNull(allRoutes);
        assertEquals(1, allRoutes.size());
    }
}