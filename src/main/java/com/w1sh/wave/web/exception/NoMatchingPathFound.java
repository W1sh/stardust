package com.w1sh.wave.web.exception;

import jakarta.servlet.ServletException;

public class NoMatchingPathFound extends ServletException {
    public NoMatchingPathFound(String message) {
        super(message);
    }
}
