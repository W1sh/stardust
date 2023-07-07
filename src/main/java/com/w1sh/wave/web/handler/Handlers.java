package com.w1sh.wave.web.handler;

public class Handlers {

    private Handlers() {
    }

    public static Handler health() {
        return (req, resp) -> "{\"status\":\"UP\"}";
    }

    public static Handler shutdown() {
        return (req, resp) -> {
            System.exit(0);
            return "Started shutdown";
        };
    }
}
