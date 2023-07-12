package com.w1sh.aperture.web;

public enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE, UNSUPPORTED;

    public static HttpMethod fromString(String method) {
        for (HttpMethod value : HttpMethod.values()) {
            if (value.name().equalsIgnoreCase(method)) return value;
        }
        return UNSUPPORTED;
    }
}
