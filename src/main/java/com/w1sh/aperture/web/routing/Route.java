package com.w1sh.aperture.web.routing;

import com.w1sh.aperture.web.HttpMethod;
import com.w1sh.aperture.web.endpoint.Endpoint;

public record Route(HttpMethod method, String path, Endpoint endpoint) {
}
