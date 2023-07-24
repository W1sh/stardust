package com.w1sh.aperture;

import com.w1sh.aperture.exception.MetadataProcessingException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record Metadata(String name, Integer priority, Class<?>[] requiredClasses, Class<?>[] requiredMissingClasses,
                       String[] profiles, Map<String, String> requiredSystemProperties, Scope scope, Boolean primary) {

    public static Builder builder() {
        return new Builder();
    }

    public static Metadata empty() {
        return Metadata.builder().build();
    }
    
    public Metadata merge(Metadata other) {
        if (this.equals(Metadata.empty())) return other;
        if (other.equals(Metadata.empty())) return this;
        return Metadata.builder()
                .name(mergeValue(this.name(), other.name()))
                .primary(mergeValue(this.primary(), other.primary()))
                .scope(mergeValue(this.scope(), other.scope()))
                .priority(mergeValue(this.priority(), other.priority()))
                .profiles(mergeValue(this.profiles(), other.profiles()))
                .conditionalOn(mergeValue(this.requiredClasses(), other.requiredClasses()))
                .conditionalOnMissing(mergeValue(this.requiredMissingClasses(), other.requiredMissingClasses()))
                .requiredSystemProperties(mergeValue(this.requiredSystemProperties(), other.requiredSystemProperties()))
                .build();
    }
    
    private <T> T mergeValue(T o1, T o2) throws MetadataProcessingException {
        if (o1 != null && o2 != null) {
            if (o1.equals(o2)) {
                return o1;
            } else throw new MetadataProcessingException("Failed to merge metadata as both have different values");
        }
        return o1 != null ? o1 : o2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metadata metadata = (Metadata) o;
        return Objects.equals(name, metadata.name) && Objects.equals(priority, metadata.priority) && Arrays.equals(requiredClasses, metadata.requiredClasses) && Arrays.equals(requiredMissingClasses, metadata.requiredMissingClasses) && Arrays.equals(profiles, metadata.profiles) && Objects.equals(requiredSystemProperties, metadata.requiredSystemProperties) && scope == metadata.scope && Objects.equals(primary, metadata.primary);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, priority, requiredSystemProperties, scope, primary);
        result = 31 * result + Arrays.hashCode(requiredClasses);
        result = 31 * result + Arrays.hashCode(requiredMissingClasses);
        result = 31 * result + Arrays.hashCode(profiles);
        return result;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "name='" + name + '\'' +
                ", priority=" + priority +
                ", requiredClasses=" + Arrays.toString(requiredClasses) +
                ", requiredMissingClasses=" + Arrays.toString(requiredMissingClasses) +
                ", profiles=" + Arrays.toString(profiles) +
                ", requiredSystemProperties=" + requiredSystemProperties +
                ", scope=" + scope +
                ", primary=" + primary +
                '}';
    }

    public static final class Builder {

        private String name;
        private Integer priority;
        private Map<String, String> requiredSystemProperties = HashMap.newHashMap(8);
        private Class<?>[] requiredClasses;
        private Class<?>[] requiredMissingClasses;
        private String[] profiles;
        private Scope scope;
        private Boolean primary;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder priority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public Builder conditionalOn(Class<?>... requiredClasses) {
            this.requiredClasses = requiredClasses;
            return this;
        }

        public Builder conditionalOnMissing(Class<?>... requiredMissingClasses) {
            this.requiredMissingClasses = requiredMissingClasses;
            return this;
        }

        public Builder profiles(String... profiles) {
            this.profiles = profiles;
            return this;
        }

        public Builder requiredSystemProperties(Map<String, String> requiredSystemProperties) {
            this.requiredSystemProperties = requiredSystemProperties;
            return this;
        }

        public Builder requiredSystemProperty(String key, String value) {
            this.requiredSystemProperties.put(key, value);
            return this;
        }

        public Builder scope(Scope scope) {
            this.scope = scope;
            return this;
        }

        public Builder primary(Boolean primary) {
            this.primary = primary;
            return this;
        }

        public Metadata build() {
            return new Metadata(name, priority, requiredClasses, requiredMissingClasses, profiles,
                    requiredSystemProperties, scope, primary);
        }
    }
}
