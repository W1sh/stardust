package com.w1sh.stardust.health;

public abstract class ProbeResult {

    private final Status status;

    protected ProbeResult(Status status) {
        this.status = status;
    }

    protected enum Status {
        UP, DOWN;
    }
}
