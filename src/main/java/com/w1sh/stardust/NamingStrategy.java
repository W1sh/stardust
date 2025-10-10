package com.w1sh.stardust;

public interface NamingStrategy {

    String generate(Class<?> clazz);

    record Qualified() implements NamingStrategy {
        @Override
        public String generate(Class<?> clazz) {
            return clazz.getPackageName() + "." + clazz.getSimpleName();
        }
    }

    record Default() implements NamingStrategy {
        @Override
        public String generate(Class<?> clazz) {
            return clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
        }
    }
}
