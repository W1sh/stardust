package com.w1sh.wave.web;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpMethodTest {

    @Test
    void should_returnEnumValue_whenOneNameMatchesProvidedString() {
        assertEquals(HttpMethod.GET, HttpMethod.fromString("GET"));
        assertEquals(HttpMethod.POST, HttpMethod.fromString("post"));
        assertEquals(HttpMethod.PUT, HttpMethod.fromString("PuT"));
        assertEquals(HttpMethod.PATCH, HttpMethod.fromString("PATCH"));
        assertEquals(HttpMethod.DELETE, HttpMethod.fromString("DELETE"));
    }

    @Test
    void should_returnUnsupported_whenNoMatchIsFound() {
        assertEquals(HttpMethod.UNSUPPORTED, HttpMethod.fromString("GETA"));
        assertEquals(HttpMethod.UNSUPPORTED, HttpMethod.fromString("ASD"));
        assertEquals(HttpMethod.UNSUPPORTED, HttpMethod.fromString("method"));
    }
}