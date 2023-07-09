package com.w1sh.wave.web.endpoint;

import com.w1sh.wave.web.DispatcherServlet;
import com.w1sh.wave.web.WaveJettyServer;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class HandlersTest {

    private WaveJettyServer waveJettyServer;
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() throws Exception {
        dispatcherServlet = Mockito.spy(new DispatcherServlet());
        waveJettyServer = new WaveJettyServer(dispatcherServlet);
        waveJettyServer.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        waveJettyServer.stop();
    }

    @Test
    void should_respondToGetHealthRequest_whenHealthEndpointIsCalledAndHealthHandlerIsRegistered() throws Exception {
        final var client = HttpClient.newBuilder().build();
        final var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/wave/health?api=1")).build();
        waveJettyServer.context(null);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Mockito.verify(dispatcherServlet, Mockito.times(2)).addRoute(any());
        Mockito.verify(dispatcherServlet, Mockito.times(1)).service(any(), any());
        assertNotNull(response);
        assertEquals(HttpStatus.OK_200, response.statusCode());
        assertEquals("{\"status\":\"UP\"}", response.body());
    }
}