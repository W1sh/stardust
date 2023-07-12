package com.w1sh.aperture.web.routing;

import com.w1sh.aperture.web.HttpMethod;
import com.w1sh.aperture.web.ApertureJettyServer;
import com.w1sh.aperture.web.endpoint.EndpointFactory;
import com.w1sh.aperture.web.http.Handler;

public class RouteBuilder {

    private static final ThreadLocal<ApertureJettyServer> staticContext = new ThreadLocal<>();

    private RouteBuilder() {
    }

    public static ApertureJettyServer staticInstance() {
        ApertureJettyServer context = staticContext.get();
        if (context == null) {
            throw new IllegalStateException("The route static API can only be used within a ApertureJettyServer.context() call.");
        }
        return context;
    }

    public static void setStaticContext(ApertureJettyServer context) {
        staticContext.set(context);
    }

    public static void clearStaticContext() {
        staticContext.remove();
    }

    public static void get(String path, Handler handler) {
        staticInstance().registerRoute(new Route(HttpMethod.GET, path, EndpointFactory.fromHandler(handler)));
    }

    public static void put(String path, Handler handler) {
        staticInstance().registerRoute(new Route(HttpMethod.PUT, path, EndpointFactory.fromHandler(handler)));
    }

    public static void patch(String path, Handler handler) {
        staticInstance().registerRoute(new Route(HttpMethod.PATCH, path, EndpointFactory.fromHandler(handler)));
    }

    public static void post(String path, Handler handler) {
        staticInstance().registerRoute(new Route(HttpMethod.POST, path, EndpointFactory.fromHandler(handler)));
    }

    public static void delete(String path, Handler handler) {
        staticInstance().registerRoute(new Route(HttpMethod.DELETE, path, EndpointFactory.fromHandler(handler)));
    }
}
