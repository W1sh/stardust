package com.w1sh.wave.web.routing;

import com.w1sh.wave.web.HttpMethod;
import com.w1sh.wave.web.handler.Handler;

public record Route(HttpMethod method, String path, Handler handler) {
}
