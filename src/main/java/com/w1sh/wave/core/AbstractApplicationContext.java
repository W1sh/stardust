package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Nullable;
import com.w1sh.wave.core.annotation.Primary;
import com.w1sh.wave.core.annotation.Qualifier;
import com.w1sh.wave.core.binding.Lazy;
import com.w1sh.wave.core.binding.LazyBinding;
import com.w1sh.wave.core.binding.Provider;
import com.w1sh.wave.core.binding.ProviderBinding;
import com.w1sh.wave.core.exception.UnsatisfiedComponentException;
import com.w1sh.wave.util.Annotations;
import com.w1sh.wave.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class AbstractApplicationContext implements Registry, Configurable, AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractApplicationContext.class);

    private final Map<String, Object> namedInstances = new ConcurrentHashMap<>(256);
    private final Map<Class<?>, Definition> definitions = new ConcurrentHashMap<>(256);
    private final Map<Class<?>, Object> instances = new ConcurrentHashMap<>(256);
    private final Map<Class<?>, ObjectProvider<?>> providers = new ConcurrentHashMap<>(256);

    private AbstractApplicationEnvironment environment;

    protected AbstractApplicationContext() {
        this.environment = ApplicationEnvironment.builder().build();
    }

    protected abstract void initialize();

    @Override
    public void register(Definition definition) {
        final var clazz = definition.getClazz();
        definitions.put(clazz, definition);

        final var provider = createObjectProvider(definition);
        providers.put(clazz, provider);

        // early initialization of the singleton instance
        final var instance = provider.singletonInstance();
        register(definition.getName(), instance);
        for (String alias : definition.getAliases()) {
            register(alias, instance);
        }
        register(clazz, instance);
    }

    private boolean hasCircularDependency(Definition definition) {
        final var parameters = deepScan(definition.getClazz());
        return parameters.stream()
                .filter(parameter -> parameter.getType().isAssignableFrom(definition.getClazz())
                        && isDefined(parameter.getType()))
                .anyMatch(parameter -> !isMatchingQualifierName(parameter, definition.getName()));
    }

    private boolean isMatchingQualifierName(AnnotatedElement annotatedElement, String qualifierName) {
        return Annotations.getAnnotationOfType(annotatedElement, Qualifier.class)
                .filter(annotation -> ((Qualifier) annotation).name().equalsIgnoreCase(qualifierName))
                .isPresent();
    }

    private boolean isDefined(Class<?> clazz) {
        for (Map.Entry<Class<?>, Definition> scopeClazz : definitions.entrySet()) {
            if (clazz.isAssignableFrom(scopeClazz.getKey())) {
                return true;
            }
        }
        return false;
    }

    // return all the components necessary to instantiate the class
    private List<Parameter> deepScan(Class<?> clazz) {
        final List<Parameter> classes = new ArrayList<>();

        final var constructor = ReflectionUtils.findInjectAnnotatedConstructor(clazz);
        if (constructor != null) {
            for (Parameter parameter : constructor.getParameters()) {
                classes.addAll(deepScan(parameter.getType()));
            }
        }
        return classes;
    }

    @Override
    public void register(Class<?> clazz, Object instance) {
        if (instances.containsKey(clazz) && !environment.isOverridingEnabled()) {
            logger.info("Instance of class {} already exists in scope", clazz);
            return;
        }
        instances.put(clazz, instance);
    }

    @Override
    public void register(String name, Object instance) {
        if (namedInstances.containsKey(name) && !environment.isOverridingEnabled()) {
            logger.info("Named instance {} already exists in scope", name);
            return;
        }
        namedInstances.put(name, instance);
    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
        if (instances.containsKey(clazz)) {
            return clazz.cast(instances.get(clazz));
        }

        final List<T> componentsOfType = getComponentsOfType(clazz);
        if (componentsOfType.isEmpty()) {
            logger.error("No injection candidate found for class {}", clazz);
            if (environment.isNullComponentsAllowed()) return null;
            throw new UnsatisfiedComponentException("No injection candidate found for class " + clazz);
        } else if (componentsOfType.size() == 1) {
            return clazz.cast(componentsOfType.get(0));
        }
        return resolveCandidates(clazz, componentsOfType);
    }

    @Override
    public Object getComponent(String name) {
        final Object component = namedInstances.getOrDefault(name, null);
        if (component == null && !environment.isNullComponentsAllowed()) {
            throw new UnsatisfiedComponentException("No injection candidate found with name " + name);
        }
        return component;
    }

    @Override
    public <T> T getComponent(String name, Class<T> clazz) {
        final Object instance = getComponent(name);
        return instance != null ? clazz.cast(instance) : null;
    }

    @Override
    public <T> List<T> getComponentsOfType(Class<T> clazz) {
        final List<T> candidates = new ArrayList<>();
        for (Map.Entry<Class<?>, Object> scopeClazz : instances.entrySet()) {
            if (clazz.isAssignableFrom(scopeClazz.getKey())) {
                candidates.add(clazz.cast(scopeClazz.getValue()));
            }
        }
        return candidates;
    }

    @Override
    public boolean containsComponent(Class<?> clazz) {
        return !getComponentsOfType(clazz).isEmpty();
    }

    @Override
    public boolean containsComponent(Class<?> clazz, boolean allowSearchSubclasses) {
        if (allowSearchSubclasses) {
            return containsComponent(clazz);
        }
        return instances.containsKey(clazz);
    }

    @Override
    public boolean containsComponent(String name) {
        return namedInstances.containsKey(name);
    }

    @Override
    public Class<?> getType(String name) {
        final Object component = getComponent(name);
        if (component != null) {
            return component.getClass();
        }
        return null;
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> clazz) {
        final Class<?> type = getType(name);
        return type != null && type.isAssignableFrom(clazz);
    }

    private <T> T resolveCandidates(Class<T> clazz, List<T> componentsOfType) {
        final List<T> primaryCandidates = componentsOfType.stream()
                .filter(component -> Annotations.isAnnotationPresent(component.getClass(), Primary.class))
                .collect(Collectors.toList());

        if (primaryCandidates.isEmpty()) {
            logger.warn("Multiple injection candidates found for class {}", clazz);
            logger.error("No primary candidate was defined for multiple injection candidates for class {}", clazz);
            throw new UnsatisfiedComponentException("Multiple injection candidates found for class " + clazz);
        } else if (primaryCandidates.size() > 1) {
            logger.error("Multiple primary injection candidates found for class {}", clazz);
            throw new UnsatisfiedComponentException("Multiple primary injection candidates found for class " + clazz);
        }
        return clazz.cast(primaryCandidates.get(0));
    }

    @Override
    public AbstractApplicationEnvironment getEnvironment() {
        return this.environment;
    }

    @Override
    public void setEnvironment(AbstractApplicationEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void close() {
        instances.clear();
        namedInstances.clear();
    }

    private ObjectProvider<?> createObjectProvider(Definition definition) {
        final Supplier<?> supplier = () -> {
            final var instance = createInstance(definition);
            processPostConstructors(definition, instance);
            return instance;
        };
        return new SimpleObjectProvider<>(supplier);
    }

    private Object createInstance(Definition definition) {
        if (definition.getInjectionPoint().getParameterTypes() == null) {
            logger.debug("Creating new instance of class {}", definition.getClazz());
            return ReflectionUtils.newInstance(definition.getInjectionPoint(), new Object[]{});
        }

        final Object[] params = new Object[definition.getInjectionPoint().getParameterTypes().length];
        for (int i = 0; i < definition.getInjectionPoint().getParameterTypes().length; i++) {
            final Type paramType = definition.getInjectionPoint().getParameterTypes()[i];
            final AnnotationMetadata metadata = definition.getInjectionPoint().getParameterAnnotationMetadata()[i];

            if (paramType instanceof ParameterizedType) {
                params[i] = resolveParameterizedType((ParameterizedType) paramType, metadata);
            } else {
                params[i] = resolvePossibleNullable((Class<?>) paramType, metadata);
            }
        }
        return ReflectionUtils.newInstance(definition.getInjectionPoint(), params);
    }

    private Object resolveParameterizedType(ParameterizedType type, AnnotationMetadata metadata) {
        final Class<?> parameterizedClazz = (Class<?>) type.getActualTypeArguments()[0];
        if (type.getRawType().equals(Lazy.class)) {
            return new LazyBinding<>(providers.get(parameterizedClazz));
        }

        if (type.getRawType().equals(Provider.class)) {
            return new ProviderBinding<>(providers.get(parameterizedClazz));
        }

        return resolvePossibleNullable((Class<?>) type.getRawType(), metadata);
    }

    private Object resolvePossibleNullable(Class<?> clazz, AnnotationMetadata metadata) {
        try {
            if (metadata != null && metadata.hasAnnotation(Qualifier.class)) {
                final var name = ((Qualifier) metadata.get(Qualifier.class)).name();
                return getComponent(name, clazz);
            } else {
                return getComponent(clazz);
            }
        } catch (UnsatisfiedComponentException e) {
            logger.error("No injection candidate found for class {}", clazz);
            if (metadata != null && metadata.hasAnnotation(Nullable.class)) {
                return null;
            }
            throw e;
        }
    }

    /**
     * Invokes all the methods annotated with {@link javax.annotation.PostConstruct} annotation for the given instance.
     *
     * @param definition The {@code Definition} for the {@code Object} instance passed.
     * @param instance   The object instance to invoke the methods on.
     */
    protected void processPostConstructors(Definition definition, Object instance) {
        try {
            for (Method postConstructorMethod : definition.getPostConstructorMethods()) {
                logger.debug("Invoking post constructor method for class {}", definition.getClazz());
                postConstructorMethod.invoke(instance);
            }
        } catch (IllegalAccessException e) {
            // throw, unable to invoke post constructor
        } catch (InvocationTargetException e) {
            // throw, unable to invoke post constructor
        }
    }
}
