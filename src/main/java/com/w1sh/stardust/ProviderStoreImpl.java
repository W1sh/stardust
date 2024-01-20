package com.w1sh.stardust;

import com.w1sh.stardust.exception.ProviderRegistrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ProviderStoreImpl implements ProviderStore {

    private static final Logger logger = LoggerFactory.getLogger(ProviderStoreImpl.class);

    private final Map<String, Class<?>> classes = new ConcurrentHashMap<>(256);
    private final Map<String, ObjectProvider<?>> named = new ConcurrentHashMap<>(256);
    private final Deque<String> registrationOrder = new ArrayDeque<>(256);

    private boolean allowOverride = true;
    private boolean ignoreOverride = false;

    @Override
    public <T> void register(String name, Class<T> clazz, ObjectProvider<T> provider) {
        Objects.requireNonNull(name, "Cannot register provider with null name");
        Objects.requireNonNull(clazz, "Cannot register provider with null class");
        Objects.requireNonNull(provider, "Cannot register provider with null provider");

        if (named.containsKey(name)) {
            if (!allowOverride) {
                throw ProviderRegistrationException.notAllowedName(name);
            }
            if (!ignoreOverride) {
                logger.warn("Provider with name {} already present, value will be overridden", name);
                logger.warn("If you would like the framework to fail on these kind of scenarios, set the property \"stardust.providers.allow-override\" to false");
                logger.warn("If you would like to ignore warnings on these kind of scenarios, set the property \"stardust.providers.ignore-override\" to true");
            }
        }
        classes.put(name, clazz);
        named.put(name, provider);
        registrationOrder.add(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ObjectProvider<T> get(String name) {
        Objects.requireNonNull(name, "Cannot get provider with null name");
        return (ObjectProvider<T>) named.get(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<ObjectProvider<T>> get(Class<T> clazz) {
        Objects.requireNonNull(clazz, "Cannot get provider with null class");
        List<ObjectProvider<T>> list = new ArrayList<>();
        for (Map.Entry<String, Class<?>> entry : classes.entrySet()) {
            if (clazz.isAssignableFrom(entry.getValue())) {
                ObjectProvider<?> value = named.get(entry.getKey());
                list.add((ObjectProvider<T>) value);
            }
        }
        return list;
    }

    @Override
    public Set<Class<?>> getAllClasses() {
        return Set.copyOf(classes.values());
    }

    @Override
    public List<ObjectProvider<?>> getAll() {
        return named.values().stream().toList();
    }

    @Override
    public List<ObjectProvider<?>> getAllOrdered() {
        return registrationOrder.stream().map(named::get).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Integer count() {
        return named.size();
    }

    public void setAllowOverride(boolean allowOverride) {
        this.allowOverride = allowOverride;
    }

    public void setIgnoreOverride(boolean ignoreOverride) {
        this.ignoreOverride = ignoreOverride;
    }
}
