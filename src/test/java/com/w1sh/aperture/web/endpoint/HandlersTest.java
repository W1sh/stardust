package com.w1sh.aperture.web.endpoint;

import com.w1sh.aperture.web.DispatcherServlet;
import com.w1sh.aperture.web.ApertureJettyServer;
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
    void should_respondToGetHealthRequest_whenHealthEndpointIsCalledAndHealthHandlerIsRegistered() throws Exception {
        final var client = HttpClient.newBuilder().build();
        final var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/aperture/health?api=1")).build();
        apertureJettyServer.context(null);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Mockito.verify(dispatcherServlet, Mockito.times(2)).addRoute(any());
        Mockito.verify(dispatcherServlet, Mockito.times(1)).service(any(), any());
        assertNotNull(response);
        assertEquals(HttpStatus.OK_200, response.statusCode());
        assertEquals("{\"status\":\"UP\"}", response.body());
    }
}