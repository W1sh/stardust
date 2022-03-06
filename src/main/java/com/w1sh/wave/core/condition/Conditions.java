package com.w1sh.wave.core.condition;

public class Conditions {

    private Conditions() {}

    public static Condition ifNotPresent(Class<?> clazz) {
        return new SingletonNotPresentCondition(clazz);
    }

    public static Condition ifNotPresent(String name) {
        return new SingletonNotPresentCondition(name);
    }
}
