package com.w1sh.stardust;

import com.w1sh.stardust.exception.ProviderRegistrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.synchronizedMap;
import static java.util.Objects.requireNonNull;

public class ProviderStoreImpl implements ProviderStore {

    private static final Logger logger = LoggerFactory.getLogger(ProviderStoreImpl.class);

    private final Map<Key, ObjectProvider<?>> providers = synchronizedMap(new LinkedHashMap<>(256));

    private boolean allowOverride = true;
    private boolean ignoreOverride = false;

    @Override
    public <T> void register(String name, Class<T> clazz, ObjectProvider<T> provider) {
        requireNonNull(name, "Cannot register provider with null name");
        requireNonNull(clazz, "Cannot register provider with null class");
        requireNonNull(provider, "Cannot register provider with null provider");

        if (providers.containsKey(new Key(name, null))) {
            if (!allowOverride) {
                throw ProviderRegistrationException.notAllowedName(name);
            }
            if (!ignoreOverride) {
                logger.warn("Provider with name {} already present, value will be overridden", name);
                logger.warn("If you would like the framework to fail on these kind of scenarios, set the property \"stardust.providers.allow-override\" to false");
                logger.warn("If you would like to ignore warnings on these kind of scenarios, set the property \"stardust.providers.ignore-override\" to true");
            }
        }
        providers.put(new Key(name, clazz), provider);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ObjectProvider<T> get(String name) {
        requireNonNull(name, "Cannot get provider with null name");
        return (ObjectProvider<T>) providers.get(new Key(name, null));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<ObjectProvider<T>> get(Class<T> clazz) {
        requireNonNull(clazz, "Cannot get provider with null class");
        List<ObjectProvider<T>> list = new ArrayList<>();
        for (Map.Entry<Key, ObjectProvider<?>> entry : providers.entrySet()) {
            if (clazz.isAssignableFrom(entry.getKey().clazz)) {
                ObjectProvider<?> value = providers.get(entry.getKey());
                list.add((ObjectProvider<T>) value);
            }
        }
        return list;
    }

    @Override
    public Set<Class<?>> getAllClasses() {
        return providers.keySet().stream()
                .map(Key::clazz)
                .collect(Collectors.toSet());
    }

    @Override
    public List<ObjectProvider<?>> getAll() {
        return providers.values().stream().toList();
    }

    @Override
    public Integer count() {
        return providers.size();
    }

    @Override
    public void clear() {
        logger.debug("Clearing all {} providers from store", providers.size());
        providers.clear();
    }

    public void setAllowOverride(boolean allowOverride) {
        this.allowOverride = allowOverride;
    }

    public void setIgnoreOverride(boolean ignoreOverride) {
        this.ignoreOverride = ignoreOverride;
    }

    private record Key(String name, Class<?> clazz) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(name, key.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
