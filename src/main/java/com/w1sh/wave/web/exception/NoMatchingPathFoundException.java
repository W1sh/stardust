package com.w1sh.wave.web.exception;

import jakarta.servlet.ServletException;

public class NoMatchingPathFoundException extends ServletException {
    public NoMatchingPathFoundException(String message) {
        super(message);
    }
}
