package com.w1sh.wave.core.builder;

public class Options {

    private String name;

    public static Options builder() {
        return new Options();
    }

    public Options withName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }
}
