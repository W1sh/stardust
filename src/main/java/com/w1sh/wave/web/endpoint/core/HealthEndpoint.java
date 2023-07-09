package com.w1sh.wave.web.endpoint.core;

import com.w1sh.wave.web.endpoint.Endpoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HealthEndpoint extends Endpoint {

    public HealthEndpoint() {
        super();
    }

    @Override
    public Object handle(HttpServletRequest req, HttpServletResponse resp) {
        return "{\"status\":\"UP\"}";
    }
}
