package com.w1sh.wave.core;

public class QualifiedNamingStrategy implements NamingStrategy {

    @Override
    public String generate(Class<?> clazz) {
        return clazz.getPackageName() + "." + clazz.getSimpleName();
    }
}
