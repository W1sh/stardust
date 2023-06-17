package com.w1sh.wave.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Handler {

    Object handle(HttpServletRequest request, HttpServletResponse response);

}
