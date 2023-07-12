package com.w1sh.aperture.core.naming;

public class QualifiedNamingStrategy implements NamingStrategy {

    @Override
    public String generate(Class<?> clazz) {
        return clazz.getPackageName() + "." + clazz.getSimpleName();
    }
}
