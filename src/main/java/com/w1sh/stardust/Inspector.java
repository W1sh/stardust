package com.w1sh.stardust;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Inspector {

    private Inspector() {}

    public static List<Class<?>> findAllSubclassesOf(String packageName, Class<?> clazz) {
        return findAllClassesUsingClassLoader(packageName)
                .stream()
                .filter(clazz::isAssignableFrom)
                .filter(aClass -> !aClass.equals(clazz))
                .toList();
    }

    public static List<Class<?>> findAllSubclassesOf(Set<Class<?>> sources, Class<?> clazz) {
        return sources.stream().map(m -> findAllSubclassesOf(m.getPackageName(), clazz))
                .flatMap(Collection::stream)
                .toList();
    }

    private static Set<Class<?>> findAllClassesUsingClassLoader(String packageName) {
        final var stream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        final var reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    private static Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }
}
