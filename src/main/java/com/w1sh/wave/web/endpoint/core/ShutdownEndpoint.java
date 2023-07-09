package com.w1sh.wave.web.endpoint.core;

import com.w1sh.wave.web.endpoint.Endpoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ShutdownEndpoint extends Endpoint {

    public ShutdownEndpoint() {
        super();
    }

    @Override
    public Object handle(HttpServletRequest req, HttpServletResponse resp) {
        System.exit(0);
        return "Started shutdown";
    }
}
