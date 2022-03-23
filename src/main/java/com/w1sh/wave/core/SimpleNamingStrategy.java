package com.w1sh.wave.core;

public class SimpleNamingStrategy implements NamingStrategy {

    @Override
    public String generate(Class<?> clazz) {
        return clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
    }
}
