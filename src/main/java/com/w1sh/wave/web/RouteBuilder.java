package com.w1sh.wave.web;

import com.w1sh.wave.web.handler.Handler;
import com.w1sh.wave.web.routing.Route;

public class RouteBuilder {

    private static final ThreadLocal<WaveJettyServer> staticContext = new ThreadLocal<>();

    private RouteBuilder() {
    }

    public static WaveJettyServer staticInstance() {
        WaveJettyServer context = staticContext.get();
        if (context == null) {
            throw new IllegalStateException("The route static API can only be used within a WaveJettyServer.context() call.");
        }
        return context;
    }

    public static void setStaticContext(WaveJettyServer context) {
        staticContext.set(context);
    }

    public static void clearStaticContext() {
        staticContext.remove();
    }

    public static void get(String path, Handler handler) {
        staticInstance().registerRoute(new Route(HttpMethod.GET, path, handler));
    }

    public static void put(String path, Handler handler) {
        staticInstance().registerRoute(new Route(HttpMethod.PUT, path, handler));
    }

    public static void patch(String path, Handler handler) {
        staticInstance().registerRoute(new Route(HttpMethod.PATCH, path, handler));
    }

    public static void post(String path, Handler handler) {
        staticInstance().registerRoute(new Route(HttpMethod.POST, path, handler));
    }

    public static void delete(String path, Handler handler) {
        staticInstance().registerRoute(new Route(HttpMethod.DELETE, path, handler));
    }
}
