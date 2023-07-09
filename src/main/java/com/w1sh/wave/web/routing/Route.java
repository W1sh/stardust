package com.w1sh.wave.web.routing;

import com.w1sh.wave.web.HttpMethod;
import com.w1sh.wave.web.endpoint.Endpoint;

public record Route(HttpMethod method, String path, Endpoint endpoint) {
}
