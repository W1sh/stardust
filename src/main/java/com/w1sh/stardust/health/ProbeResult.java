package com.w1sh.stardust.health;

import java.util.Objects;

public class ProbeResult {

    private final Status status;

    private ProbeResult(Status status) {
        this.status = status;
    }

    public static ProbeResult up() {
        return new ProbeResult(Status.UP);
    }

    public static ProbeResult down() {
        return new ProbeResult(Status.DOWN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProbeResult that = (ProbeResult) o;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }

    private enum Status {
        UP, DOWN
    }
}
