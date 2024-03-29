package com.w1sh.stardust.naming;

public class DefaultNamingStrategy implements NamingStrategy {

    @Override
    public String generate(Class<?> clazz) {
        return clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
    }
}
