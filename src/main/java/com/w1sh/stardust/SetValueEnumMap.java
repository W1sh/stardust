package com.w1sh.stardust;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SetValueEnumMap<K extends Enum<K>, V> {

    private final EnumMap<K, Set<V>> map;

    public SetValueEnumMap(Class<K> clazz) {
        this.map = new EnumMap<>(clazz);
    }

    public Set<V> get(K key) {
        return map.getOrDefault(key, Collections.emptySet());
    }

    public void put(K key, V value) {
        map.compute(key, (k, v) -> {
            final Set<V> set = v == null ? new HashSet<>() : v;
            set.add(value);
            return set;
        });
    }

    public void remove(K key, V value) {
        map.computeIfPresent(key, (k, v) -> {
            v.remove(value);
            return v;
        });
    }

    public Map<K, Set<V>> getUnderlyingEnumMap() {
        return map;
    }
}
