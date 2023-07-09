package com.w1sh.wave.web.exception;

import jakarta.servlet.ServletException;

public class RouteMatchingException extends ServletException {
    public RouteMatchingException(String message) {
        super(message);
    }
}
