package com.w1sh.aperture;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModuleInspector {

    public static final String MODULE_NAME = "aperture.module";

    private ModuleInspector() {
    }

    public static List<Class<?>> findAllInternalSubclassesOf(Class<?> clazz) {
        return ModuleInspector.find(MODULE_NAME)
                .stream()
                .filter(clazz::isAssignableFrom)
                .filter(aClass -> !aClass.equals(clazz))
                .toList();
    }

    public static List<Class<?>> findAllAnnotatedBy(Class<? extends Annotation> annotation) {
        return ModuleInspector.find(MODULE_NAME)
                .stream()
                .filter(aClass -> aClass.isAnnotationPresent(annotation))
                .toList();
    }

    private static List<Class<?>> find(String module) {
        return ModuleLayer.boot().findModule(module)
                .map(java.lang.Module::getPackages)
                .orElse(new HashSet<>())
                .stream()
                .map(ModuleInspector::findAllClassesUsingClassLoader)
                .flatMap(Collection::stream)
                .toList();
    }

    private static Set<Class<?>> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
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
