package com.w1sh.wave.core.builder;

public class Options {

    private String name;
    private Class<?>[] requiredClasses;
    private Class<?>[] requiredMissingClasses;


    public static Options builder() {
        return new Options();
    }

    public Options withName(String name) {
        this.name = name;
        return this;
    }

    public Options conditionalOn(Class<?>... requiredClasses) {
        this.requiredClasses = requiredClasses;
        return this;
    }

    public Options conditionalOnMissing(Class<?>... requiredMissingClasses) {
        this.requiredMissingClasses = requiredMissingClasses;
        return this;
    }

    public String getName() {
        return name;
    }

    public Class<?>[] getRequiredClasses() {
        return requiredClasses;
    }

    public Class<?>[] getRequiredMissingClasses() {
        return requiredMissingClasses;
    }
}
