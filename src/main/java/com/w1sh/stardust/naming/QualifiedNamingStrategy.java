package com.w1sh.stardust.naming;

public class QualifiedNamingStrategy implements NamingStrategy {

    @Override
    public String generate(Class<?> clazz) {
        return clazz.getPackageName() + "." + clazz.getSimpleName();
    }
}
