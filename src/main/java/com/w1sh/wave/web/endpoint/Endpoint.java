package com.w1sh.wave.web.endpoint;

import com.w1sh.wave.web.http.Handler;

public abstract class Endpoint implements Handler {

    private final InvocationContext context;

    protected Endpoint() {
        this.context = null;
    }

    protected Endpoint(InvocationContext context) {
        this.context = context;
    }

    public InvocationContext getContext() {
        return context;
    }
}